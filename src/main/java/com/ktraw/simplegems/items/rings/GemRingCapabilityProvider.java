package com.ktraw.simplegems.items.rings;

import com.ktraw.simplegems.util.energy.SimpleGemsItemEnergyStorage;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
public class GemRingCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<SimpleGemsItemEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);
    private final ItemStack stack;

    private SimpleGemsItemEnergyStorage createEnergyStorage() {
        return new SimpleGemsItemEnergyStorage(1000000, 1000, stack);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(
            @Nonnull final Capability<T> cap,
            @Nullable final Direction side
    ) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energy.cast();
        } else {
            return LazyOptional.empty();
        }
    }
}
