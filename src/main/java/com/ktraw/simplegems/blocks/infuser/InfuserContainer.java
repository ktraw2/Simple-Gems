package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.tools.CustomEnergyStorage;
import com.ktraw.simplegems.tools.SimpleGemsContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;


public class InfuserContainer extends SimpleGemsContainer<InfuserContainer>  implements IRecipeContainer {
    private CraftResultInventory craftResult = new CraftResultInventory();
    private CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);

    public InfuserContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModBlocks.INFUSER_CONTAINER, windowId, world, pos, playerInventory, player);

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
        return 5;
    }

    @Override
    protected void initContainerSlots() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            int index = 0;
            index = addSlotBox(h, index, 40, 25, 2, 18, 2, 18);
            addSlot(new SlotItemHandler(h, index, 116, 35));
        });
    }

    @Override
    protected Block getContainerBlockType() {
        return ModBlocks.INFUSER;
    }


    @Override
    public CraftResultInventory getCraftResult() {
        return craftResult;
    }

    @Override
    public CraftingInventory getCraftMatrix() {
        return craftMatrix;
    }
}
