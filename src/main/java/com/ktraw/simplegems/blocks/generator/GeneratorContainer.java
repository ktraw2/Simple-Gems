package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GeneratorContainer extends SimpleGemsContainer<GeneratorContainer> {
    private List<Item> validMergeItems = Arrays.asList(new Item[]{ModItems.CHARGED_EMERALD_DUST});

    /**
     * Client side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     */
    public GeneratorContainer(int windowId, Inventory playerInventory) {
        super(ModBlocks.GENERATOR_CONTAINER, windowId, playerInventory, GeneratorTile.TOTAL_SLOTS, GeneratorTile.DATA_SIZE);
    }

    /**
     * Server side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory the inventory of the container
     * @param dataIn the serverside tile entity data
     */
    public GeneratorContainer(int windowId, Inventory playerInventory, Container inventory, ContainerData dataIn) {
        super(ModBlocks.GENERATOR_CONTAINER, windowId, playerInventory, inventory, dataIn);
    }


    @Override
    protected void initContainerSlots() {
        addSlot(new Slot(inventory, GeneratorTile.FUEL_SLOT, 80, 31){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem().equals(ModItems.CHARGED_EMERALD_DUST);
            }
        });
        addSlot(new Slot(inventory, GeneratorTile.CHARGE_SLOT, 134, 49){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
            }
        });
    }

    @Override
    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.of(validMergeItems);
    }
}
