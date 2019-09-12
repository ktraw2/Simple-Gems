package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.tools.SimpleGemsEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.ktraw.simplegems.blocks.ModBlocks;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IInventory {
    public static final int TOTAL_SLOTS = 5;
    public static final int TOTAL_CRAFTING_SLOTS = TOTAL_SLOTS - 1;
    public static final int OUTPUT_SLOT_INDEX = TOTAL_SLOTS - 1;

    public static final int INT_TIMER = 0;
    public static final int INT_ENERGY = 1;
    public static final int INT_RECIPE_PROCESS_TIME = 2;
    public static final int DATA_SIZE = 3;

    private IIntArray infuserData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case INT_TIMER:
                    return timer;

                case INT_ENERGY:
                    return getEnergy();

                case INT_RECIPE_PROCESS_TIME:
                    if (currentRecipe == null) {
                        return 0;
                    }
                    else {
                        return currentRecipe.getProcessTime();
                    }

                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case INT_TIMER:
                    timer = value;
                    markDirty();

                case INT_ENERGY:
                    setEnergy(value);
                    markDirty();
            }
        }

        @Override
        public int size() {
            return DATA_SIZE;
        }
    };

    private LazyOptional<InfuserItemStackHandler> items = LazyOptional.of(this::createItemHandler);
    private LazyOptional<SimpleGemsEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    // TODO Serialize and deserialize any local fields I add here
    private int timer = 0;
    InfuserRecipe currentRecipe;

    public InfuserTile() {
        super(ModBlocks.INFUSER_TILE);
    }

    private InfuserItemStackHandler createItemHandler() {
        return new InfuserItemStackHandler(TOTAL_SLOTS, this);
    }

    private SimpleGemsEnergyStorage createEnergyStorage() {
        return new SimpleGemsEnergyStorage(10000, 100);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        items.ifPresent(h -> {
            compound.put("inventory", h.serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", h.serializeNBT());
        });
        compound.putInt("timer", timer);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        items.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("inventory"));
        });
        energy.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("energy"));
        });
        timer = compound.getInt("timer");
        super.read(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return items.cast();
        }
        else if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    private boolean canInfuse(@Nonnull InfuserRecipe recipe) {
        boolean sufficientEnergy = energy.map(e -> e.getEnergyStored() >= recipe.getEnergy()).orElse(false);
        if (!sufficientEnergy) {
            return false;
        }
        else {
            int numOccupiedSlots = 0;
            for (int i = 0; i < TOTAL_CRAFTING_SLOTS; i++) {
                if (!getStackInSlot(i).isEmpty()) {
                    numOccupiedSlots++;
                }
            }

            if (numOccupiedSlots != recipe.getIngredients().size()) {
                return false;
            }
            else {
                ItemStack recipeOutput = recipe.getRecipeOutput();
                if (recipeOutput.isEmpty()) {
                    return false;
                }
                else {
                    ItemStack infuserOutputSlot = getStackInSlot(OUTPUT_SLOT_INDEX);
                    int combinedTotal = infuserOutputSlot.getCount() + recipeOutput.getCount();
                    if (infuserOutputSlot.isEmpty()) {
                        return true;
                    } else if (!infuserOutputSlot.isItemEqual(recipeOutput)) {
                        return false;
                    }
                    else if (combinedTotal <= getInventoryStackLimit() && combinedTotal <= infuserOutputSlot.getMaxStackSize()) {
                        return true;
                    }
                    else {
                        return combinedTotal <= infuserOutputSlot.getMaxStackSize();
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (timer <= 0) {
                InfuserRecipe recipe = world.getRecipeManager().getRecipe(ModBlocks.INFUSER_RECIPE_TYPE, this, world).orElse(null);
                if (recipe != null && canInfuse(recipe)) {
                    timer = recipe.getProcessTime();
                    energy.ifPresent(e -> {
                        e.consumeEnergy(recipe.getEnergy());
                    });
                    for (int i = 0; i < TOTAL_CRAFTING_SLOTS; i++) {
                        int finalI = i;
                        items.ifPresent(h -> {
                            h.extractItem(finalI, 1, false);
                        });
                    }
                    currentRecipe = recipe;
                    markDirty();
                }
            }
            else {
                timer--;
                if (timer <= 0 && currentRecipe != null) {
                    items.ifPresent(h -> {
                        h.insertItemNoCheck(OUTPUT_SLOT_INDEX, currentRecipe.getRecipeOutput().copy(), false);
                    });
                    currentRecipe = null;
                    markDirty();
                }
            }

       }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new InfuserContainer(i, playerInventory, this, infuserData);
    }

    /* MARK IInventory */

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != OUTPUT_SLOT_INDEX;
    }

    @Override
    public int getSizeInventory() {
        return items.map(ItemStackHandler::getSlots).orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return items.map(h -> {
            boolean result = true;
            for (int i = 0; i < h.getSlots(); i++) {
                if (!h.getStackInSlot(i).equals(ItemStack.EMPTY, false)) {
                    result = false;
                    break;
                }
            }

            return result;
        }).orElse(true);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return items.map(h -> h.extractItem(index, count, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return items.map(h -> {
            ItemStack stackInSlot = h.getStackInSlot(index);
            h.setStackInSlot(index, ItemStack.EMPTY);
            return stackInSlot;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.ifPresent(h -> {
            h.setStackInSlot(index, stack);
        });
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.ifPresent(h -> {
            for (int i = 0; i < h.getSlots(); i++) {
                h.setStackInSlot(i, ItemStack.EMPTY);
            }
        });
    }

    /* end IInventory */

    public int getTimer() {
        return timer;
    }

    public int getEnergy() {
        return energy.map(EnergyStorage::getEnergyStored).orElse(0);
    }

    private void setEnergy(int value) {
        energy.ifPresent(e -> e.setEnergy(value));
    }

    public InfuserRecipe getCurrentRecipe() {
        return currentRecipe;
    }
}
