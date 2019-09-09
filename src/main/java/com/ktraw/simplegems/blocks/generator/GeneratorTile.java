package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventory {
    public static final int PROCESS_TICKS = 80;
    public static final int TOTAL_SLOTS = 1;

    private LazyOptional<ItemStackHandler> items = LazyOptional.of(this::createHandler);
    private LazyOptional<CustomEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    public static final int INT_TIMER = 0;
    public static final int INT_ENERGY = 1;
    public static final int DATA_SIZE = 2;
    private IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case INT_TIMER:
                    return getTimer();

                case INT_ENERGY:
                    return getEnergy();

                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case INT_TIMER:
                    timer = value;

                case INT_ENERGY:
                    setEnergy(value);
            }
        }

        @Override
        public int size() {
            return DATA_SIZE;
        }
    } ;

    private int timer;
    private boolean processing;

    public GeneratorTile() {
        super(ModBlocks.GENERATOR_TILE);
    }

    @Override
    public void read(CompoundNBT compound) {
        items.ifPresent(h -> h.deserializeNBT(compound.getCompound("inventory")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
        timer = compound.getInt("counter");
        processing = compound.getBoolean("processing");
        super.read(compound);
    }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        items.ifPresent(h -> {
            compound.put("inventory", h.serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", h.serializeNBT());
        });
        compound.putInt("counter", timer);
        compound.putBoolean("processing", processing);
        return super.write(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == ModItems.CHARGED_EMERALD_DUST;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != ModItems.CHARGED_EMERALD_DUST) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(100000, 100);
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
            if (timer > 0) {
                timer--;
                if (timer <= 0) {
                    energy.ifPresent(e -> {
                        if (e.getEnergyStored() < e.getMaxEnergyStored()) {
                            e.addEnergy(1000);
                        }
                    });
                    processing = false;
                }
                markDirty();
            } else {
                items.ifPresent(h -> {
                    ItemStack stack = h.getStackInSlot(0);
                    if (stack.getItem() == ModItems.CHARGED_EMERALD_DUST) {
                        energy.ifPresent(e -> {
                            if (e.getEnergyStored() < e.getMaxEnergyStored()) {
                                h.extractItem(0, 1, false);
                                timer = PROCESS_TICKS;
                                processing = true;
                                markDirty();
                            }
                        });
                    }
                });
            }
            sendOutPower();
        }
    }

    private void sendOutPower() {
        energy.ifPresent(energy -> {
            AtomicInteger storedEnergy = new AtomicInteger(energy.getEnergyStored());
            if (storedEnergy.get() > 0) {
                for (Direction direction : Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(storedEnergy.get(), 100), false);
                                storedEnergy.addAndGet(-received);
                                ((CustomEnergyStorage)energy).consumeEnergy(received);
                                markDirty();
                                return storedEnergy.get() > 0;
                            }
                            else {
                                return true;
                            }
                        }).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new GeneratorContainer(i, playerInventory, this, data);
    }

    /* MARK IInventory */

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

    // TODO See if I want to change this, look at Minecraft's actual recipe blocks to see how they handle the problem of counting the output slot
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

    /* End IInventory */

    public int getTimer() {
        return timer;
    }

    public boolean isProcessing() {
        return processing;
    }

    public int getEnergy() {
        return energy.map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    private void setEnergy(int value) {
        energy.ifPresent(e -> e.setEnergy(value));
    }
}
