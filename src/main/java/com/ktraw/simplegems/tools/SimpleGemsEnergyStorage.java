package com.ktraw.simplegems.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class SimpleGemsEnergyStorage extends EnergyStorage implements INBTSerializable<Tag> { // TODO: we want this to be compoundTag
    public SimpleGemsEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public SimpleGemsEnergyStorage(int capacity, int maxTransfer, int energy) {
        super(capacity, maxTransfer, maxTransfer, energy);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getEnergyStored();
        }
    }

    public void consumeEnergy(int energy) {
        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("energy", getEnergyStored());
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        if (nbt instanceof CompoundTag) {
            setEnergy(((CompoundTag)nbt).getInt("energy"));
        }
    }
}
