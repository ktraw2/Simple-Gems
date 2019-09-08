package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.tools.CustomEnergyStorage;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.ktraw.simplegems.blocks.ModBlocks;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserTile extends TileEntity implements ITickableTileEntity {
    private LazyOptional<ItemStackHandler> items = LazyOptional.of(this::createItemHandler);
    private LazyOptional<CustomEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    public InfuserTile() {
        super(ModBlocks.INFUSER_TILE);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(){

        };
    }

    private CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(10000, 100);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        items.ifPresent(h -> {
            compound.put("inventory", h.serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", h.serializeNBT());
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return items.cast();
        }
        else if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {

    }
}
