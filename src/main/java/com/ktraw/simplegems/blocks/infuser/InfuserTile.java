package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.ktraw.simplegems.blocks.ModBlocks;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventory {
    private LazyOptional<ItemStackHandler> items = LazyOptional.of(this::createItemHandler);
    private LazyOptional<CustomEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    public InfuserTile() {
        super(ModBlocks.INFUSER_TILE);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(5){
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot != 4) {
                    return super.insertItem(slot, stack, simulate);
                }
                else {
                    return stack;
                }
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
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
        items.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("inventory"));
        });
        energy.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("energy"));
        });
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
        if (!world.isRemote) {
            IRecipe<?> recipe = world.getRecipeManager().getRecipe(ModBlocks.INFUSER_RECIPE_TYPE, this, world).orElse(null);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new InfuserContainer(i, world, pos, playerInventory, playerEntity);
    }

    @Override
    public int getSizeInventory() {
        return items.map(ItemStackHandler::getSlots).orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return items.map(h -> {
            boolean result = true;
            for (int i = 0; i < h.getSlots(); i++) {
                if (!h.getStackInSlot(i).equals(ItemStack.EMPTY, false)) {
                    result = false;
                    break;
                }
            }

            return result;
        }).orElse(false);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return items.map(h -> {
            ItemStack stack = h.getStackInSlot(index);
            int currentCount = stack.getCount();
            stack.setCount((currentCount > count) ? currentCount - count : 0);
            stack = (stack.getCount() == 0) ? ItemStack.EMPTY : stack;
            h.setStackInSlot(index, stack);
            return stack;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return items.map(h -> {
            ItemStack stackInSlot = h.getStackInSlot(index);
            h.setStackInSlot(index, ItemStack.EMPTY);
            return stackInSlot;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.ifPresent(h -> {
            h.setStackInSlot(index, stack);
        });
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.ifPresent(h -> {
            for (int i = 0; i < h.getSlots(); i++) {
                h.setStackInSlot(i, ItemStack.EMPTY);
            }
        });
    }
}
