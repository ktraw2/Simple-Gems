package com.ktraw.simplegems.util.energy;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class SimpleGemsItemEnergyStorage extends EnergyStorage {
    private final ItemStack stack;

    public SimpleGemsItemEnergyStorage(
            final int capacity,
            final ItemStack stack
    ) {
        super(capacity);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(
            final int capacity,
            final int maxTransfer,
            final ItemStack stack
    ) {
        super(capacity, maxTransfer);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(
            final int capacity,
            final int maxReceive,
            final int maxExtract,
            final ItemStack stack
    ) {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(
            final int capacity,
            final int maxReceive,
            final int maxExtract,
            final int energy,
            final ItemStack stack
    ) {
        super(capacity, maxReceive, maxExtract, energy);
        this.stack = stack;
        // TODO do I want this behavior?
        stack.getOrCreateTag().putInt("energy", energy);
    }

    @Override
    public int receiveEnergy(
            final int maxReceive,
            final boolean simulate
    ) {
        energy = getEnergyStored();
        final int result = super.receiveEnergy(maxReceive, simulate);
        stack.getOrCreateTag().putInt("energy", energy);
        return result;
    }

    @Override
    public int extractEnergy(
            final int maxExtract,
            final boolean simulate
    ) {
        energy = getEnergyStored();
        final int result = super.extractEnergy(maxExtract, simulate);
        stack.getOrCreateTag().putInt("energy", energy);
        return result;
    }

    @Override
    public int getEnergyStored() {
        return stack.getOrCreateTag().getInt("energy");
    }
}
