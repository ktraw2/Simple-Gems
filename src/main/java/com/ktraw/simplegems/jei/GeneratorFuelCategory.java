package com.ktraw.simplegems.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu.Slots;
import com.ktraw.simplegems.blocks.generator.GeneratorFuelRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.AccessLevel;
import lombok.Getter;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static com.ktraw.simplegems.blocks.generator.GeneratorScreen.FLAME_SIDE;
import static com.ktraw.simplegems.blocks.generator.GeneratorScreen.textureLocation;
import static net.minecraft.client.gui.GuiComponent.fill;

@Getter
public class GeneratorFuelCategory implements IRecipeCategory<GeneratorFuelRecipe> {

    private static final int GUI_WIDTH = Slots.width * 4;
    private static final int GUI_HEIGHT = 34;

    private final ResourceLocation Uid;
    private final Class<? extends GeneratorFuelRecipe> recipeClass;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    @Getter(AccessLevel.NONE)
    private final LoadingCache<Integer, IDrawableAnimated> cachedFlames;

    public GeneratorFuelCategory(IGuiHelper guiHelper) {
        final ResourceLocation GUI_LOCATION = new ResourceLocation(SimpleGems.MODID, textureLocation);

        this.Uid = new ResourceLocation(SimpleGems.MODID, "jei_generator_fuel");
        this.recipeClass = GeneratorFuelRecipe.class;
        this.title = Component.translatable("gui.simplegems.jei.generator_fuel");
        this.background = guiHelper.createDrawable(GUI_LOCATION, Slots.Fuel.start_x - 1, Slots.Fuel.start_y - 1, GUI_WIDTH, GUI_HEIGHT);
        this.icon = guiHelper.createDrawable(GUI_LOCATION, 176, 0, FLAME_SIDE, FLAME_SIDE);

        this.cachedFlames = CacheBuilder.newBuilder()
                .maximumSize(1)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer processTime) {
                        return guiHelper.drawableBuilder(GUI_LOCATION, 176, 0, FLAME_SIDE, FLAME_SIDE)
                                .buildAnimated(processTime, IDrawableAnimated.StartDirection.BOTTOM, false);
                    }
                });
    }

    @Override
    public RecipeType<GeneratorFuelRecipe> getRecipeType() {
        return new RecipeType<>(getUid(), getRecipeClass());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GeneratorFuelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addItemStack(recipe.getFuel());
    }

    @Override
    public void draw(GeneratorFuelRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        final Font font = Minecraft.getInstance().font;
        final MutableComponent energy = Component.translatable("gui.simplegems.jei.energy", recipe.getEnergy());
        final MutableComponent time = Component.translatable("gui.simplegems.jei.time", recipe.getProcessTime() / 20);
        final int color = 0xFF808080;
        final int x = Slots.width + 3;
        final int y = background.getHeight() - font.lineHeight + 2;

        fill(stack, 54, 18, 72, 34, 0xFFC6C6C6);

        font.draw(stack, energy, x, (background.getHeight() / 2.0f) - font.lineHeight - 1, color);
        font.draw(stack, time, x, (background.getHeight() / 2.0f) + 1, color);

        this.cachedFlames.getUnchecked(recipe.getProcessTime()).draw(stack, 1, GUI_HEIGHT - FLAME_SIDE);
    }
}
