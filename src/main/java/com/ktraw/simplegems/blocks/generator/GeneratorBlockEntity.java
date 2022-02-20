package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerBlockEntity;
import com.ktraw.simplegems.util.energy.SimpleGemsEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ktraw.simplegems.util.containers.SimpleGemsContainerMenuFactory.SimpleGemsContainerMenuType.GENERATOR;

public class GeneratorBlockEntity extends SimpleGemsContainerBlockEntity<ItemStackHandler> {
    public static final int PROCESS_TICKS = 80;
    public static final int FUEL_SLOT = 0;
    public static final int CHARGE_SLOT = 1;
    public static final int TOTAL_SLOTS = 2;

    public static final int DATA_SIZE = 2;

    private boolean processing;

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.GENERATOR_TILE, pos, state, GENERATOR);

        this.items = LazyOptional.of(this::createHandler);
        this.energy = LazyOptional.of(this::createEnergy);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case INT_TIMER -> getTimer();
                    case INT_ENERGY -> getEnergy();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case INT_TIMER:
                        timer = value;
                        setChanged();

                    case INT_ENERGY:
                        setEnergy(value);
                        setChanged();
                }
            }

            @Override
            public int getCount() {
                return DATA_SIZE;
            }
        };
    }

    @Override
    public void load(CompoundTag compound) {
        processing = compound.getBoolean("processing");
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("processing", processing);
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

    /* MARK IInventory */

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return items.map(h -> h.isItemValid(index, stack)).orElse(false);
    }

    public int getEnergy() {
        return energy.map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public boolean isProcessing() {
        return processing;
    }
}
