package com.ktraw.simplegems.jei;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.generator.GeneratorBlockEntity;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.generator.GeneratorFuelRecipe;
import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import com.ktraw.simplegems.registry.Blocks;
import com.ktraw.simplegems.registry.Items;
import com.ktraw.simplegems.registry.Menus;
import com.ktraw.simplegems.registry.RecipeTypes;
import lombok.Getter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.ktraw.simplegems.blocks.generator.GeneratorScreen.FLAME_SIDE;

@JeiPlugin
public class SimpleGemsJEIPlugin implements IModPlugin {

    private IRecipeCategory<InfuserRecipe> infuserCategory;
    private IRecipeCategory<GeneratorFuelRecipe> generatorFuelCategory;

    @Getter
    private final ResourceLocation pluginUid = new ResourceLocation(SimpleGems.MODID, "jei");

    @Override
    public void registerCategories(final IRecipeCategoryRegistration registration) {
        final IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        infuserCategory = new InfuserRecipeCategory(guiHelper);
        generatorFuelCategory = new GeneratorFuelCategory(guiHelper);

        registration.addRecipeCategories(infuserCategory, generatorFuelCategory);
    }

    @Override
    public void registerGuiHandlers(final IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(InfuserScreen.class, 80, 32, 28, 23, infuserCategory.getRecipeType());
        registration.addRecipeClickArea(GeneratorScreen.class, 80, 50, FLAME_SIDE, FLAME_SIDE, generatorFuelCategory.getRecipeType());
    }

    @Override
    public void registerRecipeCatalysts(final IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.INFUSER.get()), infuserCategory.getRecipeType());
        registration.addRecipeCatalyst(new ItemStack(Blocks.GENERATOR.get()), generatorFuelCategory.getRecipeType());
    }

    @Override
    public void registerRecipeTransferHandlers(final IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(InfuserContainerMenu.class, Menus.INFUSER.get(), infuserCategory.getRecipeType(), 0, InfuserBlockEntity.TOTAL_CRAFTING_SLOTS, InfuserBlockEntity.TOTAL_SLOTS, 36);
        registration.addRecipeTransferHandler(GeneratorContainerMenu.class, Menus.GENERATOR.get(), generatorFuelCategory.getRecipeType(), 0, 1, GeneratorBlockEntity.TOTAL_SLOTS, 36);
    }

    @Override
    public void registerRecipes(final IRecipeRegistration registration) {
        final ItemStack chargedEmeraldDust = new ItemStack(Items.CHARGED_EMERALD_DUST.get());

        registration.addRecipes(infuserCategory.getRecipeType(), Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes.INFUSER_RECIPE_TYPE.get()));
        registration.addRecipes(generatorFuelCategory.getRecipeType(), List.of(new GeneratorFuelRecipe(chargedEmeraldDust, GeneratorBlockEntity.PROCESS_TICKS, GeneratorBlockEntity.ENERGY_PER_DUST)));

        registration.addIngredientInfo(chargedEmeraldDust, VanillaTypes.ITEM_STACK, Component.translatable("item.simplegems.charged_emerald_dust.jei_description"));
    }
}
