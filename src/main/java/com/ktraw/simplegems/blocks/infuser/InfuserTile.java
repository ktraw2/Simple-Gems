package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.tools.SimpleGemsEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserTile extends BlockEntity implements MenuProvider, Container {
    public static final int TOTAL_SLOTS = 5;
    public static final int TOTAL_CRAFTING_SLOTS = TOTAL_SLOTS - 1;
    public static final int OUTPUT_SLOT_INDEX = TOTAL_SLOTS - 1;

    public static final int INT_TIMER = 0;
    public static final int INT_ENERGY = 1;
    public static final int INT_RECIPE_PROCESS_TIME = 2;
    public static final int DATA_SIZE = 3;

    private ContainerData infuserData = new ContainerData() {
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
                    setChanged();

                case INT_ENERGY:
                    setEnergy(value);
                    setChanged();
            }
        }

        @Override
        public int getCount() {
            return DATA_SIZE;
        }
    };

    private LazyOptional<InfuserItemStackHandler> items = LazyOptional.of(this::createItemHandler);
    private LazyOptional<SimpleGemsEnergyStorage> energy = LazyOptional.of(this::createEnergyStorage);

    // TODO Serialize and deserialize any local fields I add here
    private int timer = 0;
    InfuserRecipe currentRecipe;

    public InfuserTile(BlockPos pos, BlockState state) {
        super(ModBlocks.INFUSER_TILE, pos, state);
    }

    private InfuserItemStackHandler createItemHandler() {
        return new InfuserItemStackHandler(TOTAL_SLOTS, this);
    }

    private SimpleGemsEnergyStorage createEnergyStorage() {
        return new SimpleGemsEnergyStorage(10000, 100);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        items.ifPresent(h -> {
            compound.put("inventory", h.serializeNBT());
        });
        energy.ifPresent(h -> {
            compound.put("energy", h.serializeNBT());
        });
        compound.putInt("timer", timer);
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) { // TODO: read(BlockState stateIn, CompoundTag compound)?
        items.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("inventory"));
        });
        energy.ifPresent(h -> {
            h.deserializeNBT(compound.getCompound("energy"));
        });
        timer = compound.getInt("timer");
        super.load(compound);
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
                if (!getItem(i).isEmpty()) {
                    numOccupiedSlots++;
                }
            }

            if (numOccupiedSlots != recipe.getIngredients().size()) {
                return false;
            }
            else {
                ItemStack recipeOutput = recipe.getResultItem();
                if (recipeOutput.isEmpty()) {
                    return false;
                }
                else {
                    ItemStack infuserOutputSlot = getItem(OUTPUT_SLOT_INDEX);
                    int combinedTotal = infuserOutputSlot.getCount() + recipeOutput.getCount();
                    if (infuserOutputSlot.isEmpty()) {
                        return true;
                    } else if (!infuserOutputSlot.sameItem(recipeOutput)) {
                        return false;
                    }
                    else if (combinedTotal <= getMaxStackSize() && combinedTotal <= infuserOutputSlot.getMaxStackSize()) {
                        return true;
                    }
                    else {
                        return combinedTotal <= infuserOutputSlot.getMaxStackSize();
                    }
                }
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        InfuserTile tile = (InfuserTile) be;

        // your code here
        if (!level.isClientSide) {
            if (tile.timer <= 0) {
                InfuserRecipe recipe = level.getRecipeManager().getRecipeFor(ModBlocks.INFUSER_RECIPE_TYPE, tile, level).orElse(null);
                if (recipe != null && tile.canInfuse(recipe)) {
                    tile.timer = recipe.getProcessTime();
                    tile.energy.ifPresent(e -> {
                        e.consumeEnergy(recipe.getEnergy());
                    });
                    for (int i = 0; i < TOTAL_CRAFTING_SLOTS; i++) {
                        int finalI = i;
                        tile.items.ifPresent(h -> {
                            h.extractItem(finalI, 1, false);
                        });
                    }
                    tile.currentRecipe = recipe;
                    tile.setChanged();
                }
            }
            else {
                tile.timer--;
                if (tile.timer <= 0 && tile.currentRecipe != null) {
                    tile.items.ifPresent(h -> {
                        h.insertItemNoCheck(OUTPUT_SLOT_INDEX, tile.currentRecipe.getResultItem().copy(), false);
                    });
                    tile.currentRecipe = null;
                    tile.setChanged();
                }
            }

        }
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new InfuserContainer(i, playerInventory, this, infuserData);
    }

    /* MARK IInventory */

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index != OUTPUT_SLOT_INDEX;
    }

    @Override
    public int getContainerSize() {
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
    public ItemStack getItem(int index) {
        return items.map(h -> h.getStackInSlot(index)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return items.map(h -> h.extractItem(index, count, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) { // TODO: removeStackFromSlot?
        return items.map(h -> {
            ItemStack stackInSlot = h.getStackInSlot(index);
            h.setStackInSlot(index, ItemStack.EMPTY);
            return stackInSlot;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.ifPresent(h -> {
            h.setStackInSlot(index, stack);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /* MARK Clearable */

    @Override
    public void clearContent() {
        items.ifPresent(h -> {
            for (int i = 0; i < h.getSlots(); i++) {
                h.setStackInSlot(i, ItemStack.EMPTY);
            }
        });
    }

    /* End Clearable */

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
