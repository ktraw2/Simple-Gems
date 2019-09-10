package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
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
    public GeneratorContainer(int windowId, PlayerInventory playerInventory) {
        super(ModBlocks.GENERATOR_CONTAINER, windowId, playerInventory, GeneratorTile.TOTAL_SLOTS, GeneratorTile.DATA_SIZE);
    }

    /**
     * Server side constructor
     * @param windowId the ID of the window
     * @param playerInventory the inventory of the player
     * @param inventory the inventory of the container
     * @param dataIn the serverside tile entity data
     */
    public GeneratorContainer(int windowId, PlayerInventory playerInventory, IInventory inventory, IIntArray dataIn) {
        super(ModBlocks.GENERATOR_CONTAINER, windowId, playerInventory, inventory, dataIn);
    }


    @Override
    protected void initContainerSlots() {
        addSlot(new Slot(inventory, GeneratorTile.FUEL_SLOT, 80, 31){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem().equals(ModItems.CHARGED_EMERALD_DUST);
            }
        });
        addSlot(new Slot(inventory, GeneratorTile.CHARGE_SLOT, 134, 49){
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
            }
        });
    }

    @Override
    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.of(validMergeItems);
    }
}
