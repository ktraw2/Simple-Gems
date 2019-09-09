package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
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

    private LazyOptional<ItemStackHandler> items = LazyOptional.of(this::createItemHandler);
    private LazyOptional<CustomEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    // TODO Serialize and deserialize any local fields I add here
    private int timer = 0;
    InfuserRecipe currentRecipe;

    public InfuserTile() {
        super(ModBlocks.INFUSER_TILE);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(TOTAL_SLOTS){
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
//                if (slot != OUTPUT_SLOT_INDEX) {
                    return super.insertItem(slot, stack, simulate);
//                }
//                else {
//                    return stack;
//                }
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
    }

    private CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(10000, 100);
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
                if (!getStackInAnySlot(i).isEmpty()) {
                    numOccupiedSlots++;
                }
            }

            System.out.println(recipe.getIngredients().size());
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
//                        ItemStack stack = getStackInSlot(i);
//                        if (!stack.isEmpty()){
//                            int count = stack.getCount();
//                            if (count == 1) {
//                                setInventorySlotContents(i, ItemStack.EMPTY);
//                            }
//                            else {
//                                stack.setCount(count - 1);
//                                setInventorySlotContents(i, stack);
//                            }
//                        }
                    }
                    currentRecipe = recipe;
                    markDirty();
                }
            }
            else {
                timer--;
                if (timer <= 0 && currentRecipe != null) {
                    items.ifPresent(h -> {
                        h.insertItem(OUTPUT_SLOT_INDEX, currentRecipe.getRecipeOutput().copy(), false);
                    });
//                    ItemStack stack = getStackInAnySlot(OUTPUT_SLOT_INDEX);
//                    if (!stack.isEmpty()) {
//                        stack.setCount(stack.getCount() + currentRecipe.getRecipeOutput().getCount());
//                        setInventorySlotContents(OUTPUT_SLOT_INDEX, stack);
//                    }
//                    else {
//                        setInventorySlotContents(OUTPUT_SLOT_INDEX, currentRecipe.getRecipeOutput().copy());
//                    }
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
        }).orElse(false);
    }

    // TODO See if I want to change this, look at Minecraft's actual recipe blocks to see how they handle the problem of counting the output slot
    @Override
    public ItemStack getStackInSlot(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    private ItemStack getStackInAnySlot(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return items.map(h -> {
            return h.extractItem(index, count, false);
        }).orElse(ItemStack.EMPTY);
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
