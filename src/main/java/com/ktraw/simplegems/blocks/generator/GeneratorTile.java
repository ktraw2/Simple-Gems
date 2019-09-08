package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
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

public class GeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public static final int PROCESS_TICKS = 80;

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    private int counter;
    private boolean processing;

    public GeneratorTile() {
        super(ModBlocks.GENERATOR_TILE);
    }

    @Override
    public void read(CompoundNBT compound) {
        handler.ifPresent(h -> ((INBTSerializable<INBT>)h).deserializeNBT(compound.getCompound("inventory")));
        energy.ifPresent(h -> ((INBTSerializable<INBT>)h).deserializeNBT(compound.getCompound("energy")));
        counter = compound.getInt("counter");
        processing = compound.getBoolean("processing");
        super.read(compound);
    }


    @Override
    public CompoundNBT write(CompoundNBT compound) {
        handler.ifPresent(h -> {
            compound.put("inventory", ((INBTSerializable<INBT>)h).serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", ((INBTSerializable<INBT>)h).serializeNBT());
        });
        compound.putInt("counter", counter);
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

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(100000, 100);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        else if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energy.ifPresent(e -> {
                    CustomEnergyStorage casted = (CustomEnergyStorage)e;
                    if (casted.getEnergyStored() < casted.getMaxEnergyStored()) {
                        casted.addEnergy(1000);
                    }
                });
                processing = false;
            }
            markDirty();
        }
        else {
            handler.ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(0);
                if (stack.getItem() == ModItems.CHARGED_EMERALD_DUST) {
                    energy.ifPresent(e -> {
                        CustomEnergyStorage casted = (CustomEnergyStorage)e;
                        if (casted.getEnergyStored() < casted.getMaxEnergyStored()) {
                            h.extractItem(0, 1, false);
                            counter = PROCESS_TICKS;
                            processing = true;
                            markDirty();
                        }
                    });
                }
            });
        }
        sendOutPower();
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
        return new GeneratorContainer(i, world, pos, playerInventory, playerEntity);
    }

    public int getCounter() {
        return counter;
    }

    public boolean isProcessing() {
        return processing;
    }
}
