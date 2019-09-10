package com.ktraw.simplegems.blocks.infuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InfuserItemStackHandler extends ItemStackHandler {
    private InfuserTile tile;

    public InfuserItemStackHandler(int size, InfuserTile tile) {
        super(size);
        this.tile = tile;
    }


    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return (slot == InfuserTile.OUTPUT_SLOT_INDEX) ? stack : super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    public ItemStack insertItemNoCheck(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.markDirty();
    }
}
