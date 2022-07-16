package com.ktraw.simplegems.util.containers;

import com.ktraw.simplegems.util.energy.SimpleGemsEnergyStorage;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerMenuFactory.SimpleGemsContainerMenuType;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class SimpleGemsContainerBlockEntity<I extends ItemStackHandler> extends BlockEntity implements MenuProvider, Container {
    public static final int INT_TIMER = 0;
    public static final int INT_ENERGY = 1;

    protected LazyOptional<I> items;
    protected LazyOptional<SimpleGemsEnergyStorage> energy;
    protected ContainerData data;

    @Getter
    protected int timer;

    private SimpleGemsContainerMenuType menuType;

    public SimpleGemsContainerBlockEntity(
            RegistryObject<BlockEntityType<?>> type,
            BlockPos pos,
            BlockState state,
            SimpleGemsContainerMenuType menuType
    ) {
        super(type.get(), pos, state);
        this.menuType = menuType;
        this.timer = 0;
    }

    @Override
    public void load(CompoundTag compound) {
        items.ifPresent(h -> h.deserializeNBT(compound.getCompound("inventory")));
        energy.ifPresent(h -> h.deserializeNBT(compound.getCompound("energy")));
        timer = compound.getInt("timer");
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        items.ifPresent(h -> compound.put("inventory", h.serializeNBT()));
        energy.ifPresent(h -> compound.put("energy", h.serializeNBT()));
        compound.putInt("timer", timer);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = saveWithFullMetadata();
        saveAdditional(compound);
        return compound;
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
    public Component getDisplayName() {
        return Component.literal(ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(getType()).getPath());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return SimpleGemsContainerMenuFactory.newMenu(menuType, i, playerInventory, this, data);
    }

    /* MARK IInventory */

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

    protected void setEnergy(int value) {
        energy.ifPresent(e -> e.setEnergy(value));
    }
}
