package com.ktraw.simplegems.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu.Slots;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.AccessLevel;
import lombok.Getter;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static com.ktraw.simplegems.blocks.infuser.InfuserScreen.*;

@Getter
public class InfuserRecipeCategory implements IRecipeCategory<InfuserRecipe> {
    public static final int GUI_WIDTH = 137 - (Slots.Input.start_x - 1);
    public static final int GUI_HEIGHT = (int) (Slots.Input.slot_height * 2.5);

    private final ResourceLocation Uid;
    private final Class<? extends InfuserRecipe> recipeClass;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    @Getter(AccessLevel.NONE)
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public InfuserRecipeCategory (IGuiHelper guiHelper) {
        final ResourceLocation GUI_LOCATION = new ResourceLocation(SimpleGems.MODID, textureLocation);

        this.Uid = InfuserRecipe.NAME;
        this.recipeClass = InfuserRecipe.class;
        this.title = new TranslatableComponent("block.simplegems.infuser");
        this.background = guiHelper.createDrawable(GUI_LOCATION, Slots.Input.start_x - 1, Slots.Input.start_y - 1, GUI_WIDTH, GUI_HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.INFUSER));

        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(GUI_LOCATION, 177, 0, ARROW_WIDTH, ARROW_HEIGHT)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public void draw(InfuserRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        final Font font = Minecraft.getInstance().font;
        final TranslatableComponent energy = new TranslatableComponent("gui.simplegems.jei.energy", recipe.getEnergy());
        final TranslatableComponent time = new TranslatableComponent("gui.simplegems.jei.time", recipe.getProcessTime() / 20);
        final int color = 0xFF808080;
        final int y = background.getHeight() - font.lineHeight + 2;

        font.draw(stack, energy, 0, y, color);
        font.draw(stack, time, background.getWidth() - font.width(time), y, color);

        this.cachedArrows.getUnchecked(recipe.getProcessTime()).draw(stack, 81 - Slots.Input.start_x + 2, 35 - Slots.Input.start_y);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfuserRecipe recipe, IFocusGroup focuses) {
        final NonNullList<Ingredient> ingredients = recipe.getIngredients();
        final int size = ingredients.size();

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addIngredients(ingredients.get(0));

        if (size > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, Slots.Input.slot_width + 1, 1)
                    .addIngredients(ingredients.get(1));

            if (size > 2) {
                builder.addSlot(RecipeIngredientRole.INPUT, 1, Slots.Input.slot_height + 1)
                        .addIngredients(ingredients.get(2));

                if (size > 3) {
                    builder.addSlot(RecipeIngredientRole.INPUT, Slots.Input.slot_width + 1, Slots.Input.slot_height + 1)
                            .addIngredients(ingredients.get(3));
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, Slots.Output.x - Slots.Input.start_x + 1, Slots.Output.y - Slots.Input.start_y + 1)
                .addItemStack(recipe.getResultItem());
    }
}
