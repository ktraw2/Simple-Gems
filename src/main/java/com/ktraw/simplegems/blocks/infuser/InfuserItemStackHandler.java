package com.ktraw.simplegems.blocks.infuser;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InfuserItemStackHandler extends ItemStackHandler {
    private final InfuserBlockEntity tile;

    public InfuserItemStackHandler(
            final int size,
            final InfuserBlockEntity tile
    ) {
        super(size);
        this.tile = tile;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(
            final int slot,
            @Nonnull final ItemStack stack,
            final boolean simulate
    ) {
        return (slot == InfuserBlockEntity.OUTPUT_SLOT_INDEX) ? stack : super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    public ItemStack insertItemNoCheck(
            final int slot,
            @Nonnull final ItemStack stack,
            final boolean simulate
    ) {
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
        tile.setChanged();
    }
}
