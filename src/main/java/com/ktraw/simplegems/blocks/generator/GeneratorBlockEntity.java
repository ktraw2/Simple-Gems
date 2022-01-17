package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.util.energy.SimpleGemsEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorBlockEntity extends BlockEntity implements MenuProvider, Container {
    public static final int PROCESS_TICKS = 80;
    public static final int FUEL_SLOT = 0;
    public static final int CHARGE_SLOT = 1;
    public static final int TOTAL_SLOTS = 2;

    private LazyOptional<ItemStackHandler> items = LazyOptional.of(this::createHandler);
    private LazyOptional<SimpleGemsEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    public static final int INT_TIMER = 0;
    public static final int INT_ENERGY = 1;
    public static final int DATA_SIZE = 2;
    private ContainerData data = new ContainerData() {
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
        public int getCount() {
            return DATA_SIZE;
        }
    } ;

    private int timer;
    private boolean processing;

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.GENERATOR_TILE, pos, state);
    }

    @Override
    public void load(CompoundTag compound) { // TODO: read(BlockState stateIn, CompoundTag compound)?
        items.ifPresent(h -> h.deserializeNBT(compound.getCompound("inventory")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
        timer = compound.getInt("counter");
        processing = compound.getBoolean("processing");
        super.load(compound);
    }


    @Override
    public CompoundTag save(CompoundTag compound) {
        items.ifPresent(h -> {
            compound.put("inventory", h.serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", h.serializeNBT());
        });
        compound.putInt("counter", timer);
        compound.putBoolean("processing", processing);
        return super.save(compound);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(TOTAL_SLOTS) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case FUEL_SLOT:
                        return stack.getItem() == ModItems.CHARGED_EMERALD_DUST;

                    case CHARGE_SLOT:
                        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();

                    default:
                        return false;
                }
            }
        };
    }

    private SimpleGemsEnergyStorage createEnergy() {
        return new SimpleGemsEnergyStorage(100000, 100);
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

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        GeneratorBlockEntity tile = (GeneratorBlockEntity) be;

        // your code here
        if (!level.isClientSide) {
            if (tile.timer > 0) {
                tile.timer--;
                if (tile.timer <= 0) {
                    tile.energy.ifPresent(e -> {
                        if (e.getEnergyStored() < e.getMaxEnergyStored()) {
                            e.addEnergy(1000);
                        }
                    });
                    tile.processing = false;
                }
                tile.setChanged();
            } else {
                tile.items.ifPresent(h -> {
                    ItemStack stack = h.getStackInSlot(FUEL_SLOT);
                    if (stack.getItem() == ModItems.CHARGED_EMERALD_DUST) {
                        tile.energy.ifPresent(e -> {
                            if (e.getEnergyStored() < e.getMaxEnergyStored()) {
                                h.extractItem(FUEL_SLOT, 1, false);
                                tile.timer = PROCESS_TICKS;
                                tile.processing = true;
                                tile.setChanged();
                            }
                        });
                    }
                });
            }

            boolean sendPowerToBlocks = tile.items.map(h -> {
                ItemStack stack = h.getStackInSlot(CHARGE_SLOT);
                if (stack.isEmpty()) {
                    return true;
                }
                else {
                    tile.energizeItem(stack);
                    return false;
                }
            }).orElse(true);

            if (sendPowerToBlocks) {
                tile.sendOutPower();
            }
        }
    }

    private void energizeItem(@Nonnull ItemStack stack) {
        energy.ifPresent(energy -> {
            AtomicInteger storedEnergy = new AtomicInteger(energy.getEnergyStored());
            if (storedEnergy.get() > 0) {
                stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                    int received = e.receiveEnergy(Math.min(storedEnergy.get(), 100), false);
                    storedEnergy.addAndGet(-received);
                    energy.consumeEnergy(received);
                    setChanged();
                });
            }
        });
    }

    private void sendOutPower() {
        energy.ifPresent(energy -> {
            AtomicInteger storedEnergy = new AtomicInteger(energy.getEnergyStored());
            if (storedEnergy.get() > 0) {
                for (Direction direction : Direction.values()) {
                    BlockEntity te = level.getBlockEntity(worldPosition.offset(direction.getNormal()));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(storedEnergy.get(), 100), false);
                                storedEnergy.addAndGet(-received);
                                energy.consumeEnergy(received);
                                setChanged();
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
    public Component getDisplayName() {
        return new TextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new GeneratorContainerMenu(i, playerInventory, this, data);
    }

    /* MARK IInventory */

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return items.map(h -> h.isItemValid(index, stack)).orElse(false);
    }

    @Override
    public int getContainerSize() {
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
        }).orElse(true);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return items.map(h -> h.extractItem(index, count, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) { // TODO: removeStackFromSlot?
        return items.map(h -> {
            ItemStack stackInSlot = h.getStackInSlot(index);
            h.setStackInSlot(index, ItemStack.EMPTY);
            return stackInSlot;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.ifPresent(h -> {
            h.setStackInSlot(index, stack);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /* MARK Clearable */

    @Override
    public void clearContent() {
        items.ifPresent(h -> {
            for (int i = 0; i < h.getSlots(); i++) {
                h.setStackInSlot(i, ItemStack.EMPTY);
            }
        });
    }

    /* End Clearable */

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
