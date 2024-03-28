package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.registry.Menus;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class InfuserContainerMenu extends SimpleGemsContainerMenu<InfuserContainerMenu> {

    public static final class Slots {
        public static final class Input {
            public static final int start_x = 40;
            public static final int start_y = 25;
            public static final int slots_per_row = 2;
            public static final int slot_width = 18;
            public static final int slots_per_col = 2;
            public static final int slot_height = 18;
        }

        public static final class Output {
            public static final int x = 116;
            public static final int y = 35;
        }
    }

    /**
     * Client side constructor
     *
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     */
    public InfuserContainerMenu(
            final int windowId,
            final Inventory playerInventory
    ) {
        super(Menus.INFUSER, windowId, playerInventory, InfuserBlockEntity.TOTAL_SLOTS, InfuserBlockEntity.DATA_SIZE);
    }

    /**
     * Server side constructor
     *
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory       the inventory of the container
     * @param dataIn          the serverside tile entity data
     */
    public InfuserContainerMenu(
            final int windowId,
            final Inventory playerInventory,
            final Container inventory,
            final ContainerData dataIn
    ) {
        super(Menus.INFUSER, windowId, playerInventory, inventory, dataIn);
    }

    @Override
    protected void initContainerSlots() {
        int index = 0;
        index = addSlotBox(inventory, index, Slots.Input.start_x, Slots.Input.start_y, Slots.Input.slots_per_row, Slots.Input.slot_width, Slots.Input.slots_per_col, Slots.Input.slot_height);
        addSlot(new Slot(inventory, index, Slots.Output.x, Slots.Output.y) {
            /**
             * Specify that this slot cannot be inserted to because it is the output
             * @param stack the item stack being asked to go into the slot
             * @return false, don't allow insertion into this slot
             */
            @Override
            public boolean mayPlace(@Nonnull final ItemStack stack) {
                return false;
            }
        });
    }
}
