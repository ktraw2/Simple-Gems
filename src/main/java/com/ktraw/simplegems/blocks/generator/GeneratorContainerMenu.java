package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.registry.Items;
import com.ktraw.simplegems.registry.Menus;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GeneratorContainerMenu extends SimpleGemsContainerMenu<GeneratorContainerMenu> {
    public static final class Slots {
        public static final int width = 18;

        public static final class Fuel {
            public static final int start_x = 80;
            public static final int start_y = 31;
        }

        public static final class Charge {
            public static final int start_x = 134;
            public static final int start_y = 49;
        }
    }

    private final List<Item> validMergeItems = Arrays.asList(new Item[]{Items.CHARGED_EMERALD_DUST.get()});

    /**
     * Client side constructor
     *
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     */
    public GeneratorContainerMenu(
            final int windowId,
            final Inventory playerInventory
    ) {
        super(Menus.GENERATOR, windowId, playerInventory, GeneratorBlockEntity.TOTAL_SLOTS, GeneratorBlockEntity.DATA_SIZE);
    }

    /**
     * Server side constructor
     *
     * @param windowId        the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory       the inventory of the container
     * @param dataIn          the serverside tile entity data
     */
    public GeneratorContainerMenu(
            final int windowId,
            final Inventory playerInventory,
            final Container inventory,
            final ContainerData dataIn
    ) {
        super(Menus.GENERATOR, windowId, playerInventory, inventory, dataIn);
    }


    @Override
    protected void initContainerSlots() {
        addSlot(new Slot(inventory, GeneratorBlockEntity.FUEL_SLOT, Slots.Fuel.start_x, Slots.Fuel.start_y) {
            @Override
            public boolean mayPlace(@Nonnull final ItemStack stack) {
                return stack.getItem().equals(Items.CHARGED_EMERALD_DUST.get());
            }
        });
        addSlot(new Slot(inventory, GeneratorBlockEntity.CHARGE_SLOT, Slots.Charge.start_x, Slots.Charge.start_y) {
            @Override
            public boolean mayPlace(@Nonnull final ItemStack stack) {
                return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
            }
        });
    }

    @Override
    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.of(validMergeItems);
    }
}
