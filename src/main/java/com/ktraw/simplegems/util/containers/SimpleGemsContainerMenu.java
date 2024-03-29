package com.ktraw.simplegems.util.containers;

import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public abstract class SimpleGemsContainerMenu<T extends AbstractContainerMenu> extends AbstractContainerMenu {
    protected final IItemHandler playerInventory;
    protected final Container inventory;
    protected final int slots;
    protected final ContainerData data;

    /**
     * Client side constructor, fills in data that client doesn't know with default values
     *
     * @param type            type of the container
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     */
    protected SimpleGemsContainerMenu(
            final RegistryObject<MenuType<T>> type,
            final int windowId,
            final Inventory playerInventory,
            final int inventorySize,
            final int dataSize
    ) {
        this(type, windowId, playerInventory, new SimpleContainer(inventorySize), new SimpleContainerData(dataSize));
    }

    /**
     * Server side constructor
     *
     * @param type            type of the container
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory       the inventory container
     * @param data            the serverside tile entity data
     */
    protected SimpleGemsContainerMenu(
            final RegistryObject<MenuType<T>> type,
            final int windowId,
            final Inventory playerInventory,
            final Container inventory,
            final ContainerData data
    ) {
        super(type.get(), windowId);
        this.playerInventory = new PlayerInvWrapper(playerInventory);
        this.inventory = inventory;
        this.slots = inventory.getContainerSize();
        this.data = data;

        initContainerSlots();
        layoutPlayerInventorySlots(8, 84);

        addDataSlots(data);
    }

    protected abstract void initContainerSlots();

    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.empty();
    }

    /**
     * Handle shift-clicking, merge valid items into container, otherwise merge within inventory
     *
     * @param playerIn the player that is transferring
     * @param index    the index they are transferring from
     *
     * @return the ItemStack transferred if successful, ItemStack.EMPTY if no transfer occurred
     */
    @Nonnull
    @Override
    public ItemStack quickMoveStack(
            @Nonnull final Player playerIn,
            final int index
    ) {
        final Optional<List<Item>> validMergeItems = this.getValidMergeItems();
        ItemStack itemStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);

        if (slot != null && slot.hasItem()) {
            final ItemStack stack = slot.getItem();
            itemStack = stack.copy();

            if (index < slots) {
                if (!moveItemStackTo(stack, slots, slots + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack); // TODO: onSlotChange?
            } else {
                if (validMergeItems.map(h -> h.contains(stack.getItem())).orElse(false)) {
                    if (!this.moveItemStackTo(stack, 0, slots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < slots + 27) {
                    if (!this.moveItemStackTo(stack, slots + 27, slots + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < slots + 36) {
                    if (!this.moveItemStackTo(stack, slots, slots + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(@Nonnull final Player playerIn) { // TODO: canInteractWith?
        return true;
        // TODO: return inventory.isUsableByPlayer(playerIn);
    }

    public int getEnergy() {
        return data.get(InfuserBlockEntity.INT_ENERGY);
    }

    protected int addSlotRow(
            final IItemHandler handler,
            int index,
            int x,
            final int y,
            final int width,
            final int dx
    ) {
        for (int i = 0; i < width; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(
            final IItemHandler handler,
            int index,
            final int x,
            int y,
            final int width,
            final int dx,
            final int height,
            final int dy
    ) {
        for (int i = 0; i < height; i++) {
            index = addSlotRow(handler, index, x, y, width, dx);
            y += dy;
        }
        return index;
    }


    protected int addSlotRow(
            final Container inventory,
            int index,
            int x,
            final int y,
            final int width,
            final int dx
    ) {
        for (int i = 0; i < width; i++) {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(
            final Container inventory,
            int index,
            final int x,
            int y,
            final int width,
            final int dx,
            final int height,
            final int dy
    ) {
        for (int i = 0; i < height; i++) {
            index = addSlotRow(inventory, index, x, y, width, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(
            final int leftCol,
            int topRow
    ) {
        // Inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRow(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public int getDataAt(final int index) {
        return data.get(index);
    }
}
