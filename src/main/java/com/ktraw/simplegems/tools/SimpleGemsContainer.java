package com.ktraw.simplegems.tools;

import com.ktraw.simplegems.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;
import java.util.Optional;


public abstract class SimpleGemsContainer<T extends Container> extends Container {
    protected TileEntity tileEntity;
    protected PlayerEntity player;
    protected IItemHandler playerInventory;
    private int slots;

    public SimpleGemsContainer(ContainerType<T> type, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(type, windowId);
        tileEntity = world.getTileEntity(pos);
        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.slots = getSlots();

        initContainerSlots();
        layoutPlayerInventorySlots(8, 84);
    }

    public abstract int getSlots();

    protected abstract void initContainerSlots();

    protected abstract Block getContainerBlockType();

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
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), player, getContainerBlockType());
    }

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
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

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRow(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }
}
