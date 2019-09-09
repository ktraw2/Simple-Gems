package com.ktraw.simplegems.tools;

import com.ktraw.simplegems.blocks.infuser.InfuserTile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;
import java.util.Optional;


public abstract class SimpleGemsContainer<T extends Container> extends Container {
    //protected TileEntity tileEntity;
    protected IItemHandler playerInventory;
    protected IInventory inventory;
    protected int slots;
    protected IIntArray data;

    /**
     * Client side constructor, fills in data that client doesn't know with default values
     * @param type
     * @param windowId
     * @param playerInventory
     */
    protected SimpleGemsContainer(ContainerType type, int windowId, PlayerInventory playerInventory, int inventorySize, int dataSize) {
        this(type, windowId, playerInventory, new Inventory(inventorySize), new IntArray(dataSize));
    }

    /**
     * Server side constructor
     * @param type type of the container
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory the inventory of the container
     * @param data the serverside tile entity data
     */
    protected SimpleGemsContainer(ContainerType<T> type, int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray data) {
        super(type, windowId);
        this.playerInventory = new InvWrapper(playerInventory);
        this.inventory = inventory;
        this.slots = inventory.getSizeInventory();
        this.data = data;

        initContainerSlots();
        layoutPlayerInventorySlots(8, 84);
        trackIntArray(data);
    }

    protected abstract void initContainerSlots();

    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.empty();
    }

    /**
     * Handle shift-clicking, merge valid items into container, otherwise merge within inventory
     * @param playerIn the player that is transferring
     * @param index the index they are transferring from
     * @return the ItemStack transferred if successful, ItemStack.EMPTY if no transfer occurred
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Optional<List<Item>> validMergeItems = this.getValidMergeItems();
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if (index < slots) {
                if (!mergeItemStack(stack, slots, slots + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else {
                if (validMergeItems.map(h -> h.contains(stack.getItem())).orElse(false)) {
                    if (!this.mergeItemStack(stack, 0, slots, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < slots + 27) {
                    if (!this.mergeItemStack(stack, slots + 27, slots + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < slots + 36) {
                    if (!this.mergeItemStack(stack, slots, slots + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return inventory.isUsableByPlayer(playerIn);
    }

    public int getEnergy() {
        return data.get(InfuserTile.INT_ENERGY);
    }

    protected int addSlotRow(IItemHandler handler, int index, int x, int y, int width, int dx) {
        for (int i = 0; i < width; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int width, int dx, int height, int dy) {
        for (int i = 0; i < height; i++) {
            index = addSlotRow(handler, index, x, y, width, dx);
            y += dy;
        }
        return index;
    }


    protected int addSlotRow(IInventory inventory, int index, int x, int y, int width, int dx) {
        for (int i = 0; i < width; i++) {
            addSlot(new Slot(inventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IInventory inventory, int index, int x, int y, int width, int dx, int height, int dy) {
        for (int i = 0; i < height; i++) {
            index = addSlotRow(inventory, index, x, y, width, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRow(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public int getDataAt(int index) {
        return data.get(index);
    }
    /*public TileEntity getTileEntity() {
        return tileEntity;
    }*/
}
