package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.SimpleGems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InfuserRecipe implements IRecipe<InfuserTile> {

    private IRecipeType<InfuserRecipe> type = IRecipeType.register(SimpleGems.MODID + ":infuser");

    @Override
    public boolean matches(InfuserTile inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InfuserTile inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return null;
    }
}
