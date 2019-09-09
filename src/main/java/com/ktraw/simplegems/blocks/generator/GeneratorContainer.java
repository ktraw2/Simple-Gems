package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
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

    public GeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModBlocks.GENERATOR_CONTAINER, windowId, world, pos, playerInventory, player);

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(h -> h.getEnergyStored()).orElse(0);
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
            }
        });
    }


    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    protected void initContainerSlots() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0, 80, 31));
        });
    }

    @Override
    protected Block getContainerBlockType() {
        return ModBlocks.GENERATOR;
    }

    @Override
    protected Optional<List<Item>> getValidMergeItems() {
        return Optional.of(validMergeItems);
    }
}
