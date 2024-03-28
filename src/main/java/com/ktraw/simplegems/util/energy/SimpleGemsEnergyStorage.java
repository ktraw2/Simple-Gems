package com.ktraw.simplegems.util.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class SimpleGemsEnergyStorage extends EnergyStorage implements INBTSerializable<Tag> { // TODO: we want this to be compoundTag
    public SimpleGemsEnergyStorage(
            final int capacity,
            final int maxTransfer
    ) {
        super(capacity, maxTransfer);
    }

    public SimpleGemsEnergyStorage(
            final int capacity,
            final int maxTransfer,
            final int energy
    ) {
        super(capacity, maxTransfer, maxTransfer, energy);
    }

    public void setEnergy(final int energy) {
        this.energy = energy;
    }

    public void addEnergy(final int energy) {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getEnergyStored();
        }
    }

    public void consumeEnergy(final int energy) {
        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(final Tag nbt) {
        if (nbt instanceof CompoundTag) {
            setEnergy(((CompoundTag) nbt).getInt("energy"));
        }
    }
}
