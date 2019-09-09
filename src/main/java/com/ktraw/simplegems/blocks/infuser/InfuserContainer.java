package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;


public class InfuserContainer extends SimpleGemsContainer<InfuserContainer> {

    /**
     * Client side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     */
    public InfuserContainer(int windowId, PlayerInventory playerInventory) {
        super(ModBlocks.INFUSER_CONTAINER, windowId, playerInventory, InfuserTile.TOTAL_SLOTS, InfuserTile.DATA_SIZE);
    }

    /**
     * Server side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory the inventory of the container
     * @param dataIn the serverside tile entity data
     */
    public InfuserContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray dataIn) {
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
            public boolean isItemValid(ItemStack stack) {
                return false;
            }
        });
    }
}
