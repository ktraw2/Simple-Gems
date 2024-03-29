package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.registry.BlockEntities;
import com.ktraw.simplegems.registry.Items;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerBlockEntity;
import com.ktraw.simplegems.util.energy.SimpleGemsEnergyStorage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ktraw.simplegems.util.containers.SimpleGemsContainerMenuFactory.SimpleGemsContainerMenuType.GENERATOR;

@Getter
public class GeneratorBlockEntity extends SimpleGemsContainerBlockEntity<ItemStackHandler> {
    public static final int PROCESS_TICKS = 80;
    public static final int ENERGY_PER_DUST = 10000;
    public static final int ENERGY_TRANSFER_RATE = 1000;

    public static final int FUEL_SLOT = 0;
    public static final int CHARGE_SLOT = 1;
    public static final int TOTAL_SLOTS = 2;

    public static final int DATA_SIZE = 2;

    private boolean processing;

    public GeneratorBlockEntity(
            final BlockPos pos,
            final BlockState state
    ) {
        super(BlockEntities.GENERATOR, pos, state, GENERATOR);

        this.items = LazyOptional.of(this::createHandler);
        this.energy = LazyOptional.of(this::createEnergy);
        this.data = new ContainerData() {
            @Override
            public int get(final int index) {
                return switch (index) {
                    case INT_TIMER -> getTimer();
                    case INT_ENERGY -> getEnergy();
                    default -> 0;
                };
            }

            @Override
            public void set(
                    final int index,
                    final int value
            ) {
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
    public void load(final CompoundTag compound) {
        processing = compound.getBoolean("processing");
        super.load(compound);
    }

    @Override
    protected void saveAdditional(final CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("processing", processing);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(TOTAL_SLOTS) {
            @Override
            protected void onContentsChanged(final int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(
                    final int slot,
                    @Nonnull final ItemStack stack
            ) {
                return switch (slot) {
                    case FUEL_SLOT -> stack.getItem().equals(Items.CHARGED_EMERALD_DUST.get());
                    case CHARGE_SLOT -> stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
                    default -> false;
                };
            }
        };
    }

    private SimpleGemsEnergyStorage createEnergy() {
        return new SimpleGemsEnergyStorage(1000000, ENERGY_TRANSFER_RATE);
    }

    public static <T extends BlockEntity> void tick(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final T be
    ) {
        final GeneratorBlockEntity tile = (GeneratorBlockEntity) be;

        // your code here
        if (!level.isClientSide) {
            if (tile.timer > 0) {
                tile.timer--;
                tile.energy.ifPresent(e -> {
                    if (e.getEnergyStored() < e.getMaxEnergyStored()) {
                        e.addEnergy(ENERGY_PER_DUST / PROCESS_TICKS);
                    }
                });
                if (tile.timer <= 0) {
                    tile.processing = false;
                }
                tile.setChanged();
            } else {
                tile.items.ifPresent(h -> {
                    final ItemStack stack = h.getStackInSlot(FUEL_SLOT);
                    if (stack.getItem().equals(Items.CHARGED_EMERALD_DUST.get())) {
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

            final boolean sendPowerToBlocks = tile.items.map(h -> {
                final ItemStack stack = h.getStackInSlot(CHARGE_SLOT);
                if (stack.isEmpty()) {
                    return true;
                } else {
                    tile.energizeItem(stack);
                    return false;
                }
            }).orElse(true);

            if (sendPowerToBlocks) {
                tile.sendOutPower();
            }
        }
    }

    private void energizeItem(@Nonnull final ItemStack stack) {
        energy.ifPresent(energy -> {
            final AtomicInteger storedEnergy = new AtomicInteger(energy.getEnergyStored());
            if (storedEnergy.get() > 0) {
                stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(e -> {
                    final int received = e.receiveEnergy(Math.min(storedEnergy.get(), ENERGY_TRANSFER_RATE), false);
                    storedEnergy.addAndGet(-received);
                    energy.consumeEnergy(received);
                    setChanged();
                });
            }
        });
    }

    private void sendOutPower() {
        energy.ifPresent(energy -> {
            final AtomicInteger storedEnergy = new AtomicInteger(energy.getEnergyStored());
            if (storedEnergy.get() > 0) {
                for (final Direction direction : Direction.values()) {
                    final BlockEntity te = level.getBlockEntity(worldPosition.offset(direction.getNormal()));
                    if (te != null) {
                        final boolean doContinue = te.getCapability(ForgeCapabilities.ENERGY, direction).map(handler -> {
                            if (handler.canReceive()) {
                                final int received = handler.receiveEnergy(Math.min(storedEnergy.get(), ENERGY_TRANSFER_RATE), false);
                                storedEnergy.addAndGet(-received);
                                energy.consumeEnergy(received);
                                setChanged();
                                return storedEnergy.get() > 0;
                            } else {
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
    public boolean canPlaceItem(
            final int index,
            @Nonnull final ItemStack stack
    ) {
        return items.map(h -> h.isItemValid(index, stack)).orElse(false);
    }

    public int getEnergy() {
        return energy.map(IEnergyStorage::getEnergyStored).orElse(0);
    }
}
