package com.ktraw.simplegems.items.rings;

import com.ktraw.simplegems.tools.SimpleGemsItemEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GemRingCapabilityProvider implements ICapabilityProvider {
    LazyOptional<SimpleGemsItemEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);
    ItemStack stack;

    public GemRingCapabilityProvider(ItemStack stack) {
        this.stack = stack;
    }

    private SimpleGemsItemEnergyStorage createEnergyStorage() {
        return new SimpleGemsItemEnergyStorage(100000, 100, stack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        else {
            return LazyOptional.empty();
        }
    }
}
