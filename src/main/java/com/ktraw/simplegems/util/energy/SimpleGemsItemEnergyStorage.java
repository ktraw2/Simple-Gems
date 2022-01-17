package com.ktraw.simplegems.util.energy;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class SimpleGemsItemEnergyStorage extends EnergyStorage {
    private final ItemStack stack;

    public SimpleGemsItemEnergyStorage(int capacity, ItemStack stack) {
        super(capacity);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(int capacity, int maxTransfer, ItemStack stack) {
        super(capacity, maxTransfer);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(int capacity, int maxReceive, int maxExtract, ItemStack stack) {
        super(capacity, maxReceive, maxExtract);
        this.stack = stack;
        this.energy = getEnergyStored();
    }

    public SimpleGemsItemEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, ItemStack stack) {
        super(capacity, maxReceive, maxExtract, energy);
        this.stack = stack;
        // TODO do I want this behavior?
        stack.getOrCreateTag().putInt("energy", energy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        energy = getEnergyStored();
        int result = super.receiveEnergy(maxReceive, simulate);
        stack.getOrCreateTag().putInt("energy", energy);
        return result;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        energy = getEnergyStored();
        int result = super.extractEnergy(maxExtract, simulate);
        stack.getOrCreateTag().putInt("energy", energy);
        return result;
    }

    @Override
    public int getEnergyStored() {
        return stack.getOrCreateTag().getInt("energy");
    }
}
