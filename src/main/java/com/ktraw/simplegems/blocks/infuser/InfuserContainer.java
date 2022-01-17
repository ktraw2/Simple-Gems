package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class InfuserContainer extends SimpleGemsContainer<InfuserContainer> {

    /**
     * Client side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     */
    public InfuserContainer(int windowId, Inventory playerInventory) {
        super(ModBlocks.INFUSER_CONTAINER, windowId, playerInventory, InfuserTile.TOTAL_SLOTS, InfuserTile.DATA_SIZE);
    }

    /**
     * Server side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory the inventory of the container
     * @param dataIn the serverside tile entity data
     */
    public InfuserContainer(int windowId, Inventory playerInventory, Container inventory, ContainerData dataIn) {
        super(ModBlocks.INFUSER_CONTAINER, windowId, playerInventory, inventory, dataIn);
    }

    @Override
    protected void initContainerSlots() {
        int index = 0;
        index = addSlotBox(inventory, index, 40, 25, 2, 18, 2, 18);
        addSlot(new Slot(inventory, index, 116, 35) {
            /**
             * Specify that this slot cannot be inserted to because it is the output
             * @param stack the item stack being asked to go into the slot
             * @return false, don't allow insertion into this slot
             */
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
    }
}
