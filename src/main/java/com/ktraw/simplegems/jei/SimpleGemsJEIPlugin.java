package com.ktraw.simplegems.jei;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.generator.GeneratorBlockEntity;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.generator.GeneratorFuelRecipe;
import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import com.ktraw.simplegems.items.ModItems;
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
import net.minecraft.network.chat.TranslatableComponent;
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
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        infuserCategory = new InfuserRecipeCategory(guiHelper);
        generatorFuelCategory = new GeneratorFuelCategory(guiHelper);

        registration.addRecipeCategories(infuserCategory, generatorFuelCategory);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(InfuserScreen.class, 80, 32, 28, 23, infuserCategory.getRecipeType());
        registration.addRecipeClickArea(GeneratorScreen.class, 80, 50, FLAME_SIDE, FLAME_SIDE, generatorFuelCategory.getRecipeType());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER), infuserCategory.getRecipeType());
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GENERATOR), generatorFuelCategory.getRecipeType());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(InfuserContainerMenu.class, infuserCategory.getRecipeType(), 0, InfuserBlockEntity.TOTAL_CRAFTING_SLOTS, InfuserBlockEntity.TOTAL_SLOTS, 36);
        registration.addRecipeTransferHandler(GeneratorContainerMenu.class, generatorFuelCategory.getRecipeType(), 0, 1, GeneratorBlockEntity.TOTAL_SLOTS, 36);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final ItemStack chargedEmeraldDust = new ItemStack(ModItems.CHARGED_EMERALD_DUST);

        registration.addRecipes(infuserCategory.getRecipeType(), Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModBlocks.INFUSER_RECIPE_TYPE));
        registration.addRecipes(generatorFuelCategory.getRecipeType(), List.of(new GeneratorFuelRecipe(chargedEmeraldDust, GeneratorBlockEntity.PROCESS_TICKS, GeneratorBlockEntity.ENERGY_PER_DUST)));

        registration.addIngredientInfo(chargedEmeraldDust, VanillaTypes.ITEM_STACK, new TranslatableComponent("item.simplegems.charged_emerald_dust.jei_description"));
    }
}
