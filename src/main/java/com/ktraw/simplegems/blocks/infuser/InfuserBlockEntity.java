package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.util.containers.SimpleGemsContainerBlockEntity;
import com.ktraw.simplegems.util.energy.SimpleGemsEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;

import static com.ktraw.simplegems.util.containers.SimpleGemsContainerMenuFactory.SimpleGemsContainerMenuType.INFUSER;

public class InfuserBlockEntity extends SimpleGemsContainerBlockEntity<InfuserItemStackHandler> {
    public static final int TOTAL_SLOTS = 5;
    public static final int TOTAL_CRAFTING_SLOTS = TOTAL_SLOTS - 1;
    public static final int OUTPUT_SLOT_INDEX = TOTAL_SLOTS - 1;

    public static final int INT_RECIPE_PROCESS_TIME = 2;
    public static final int DATA_SIZE = 3;

    // TODO Serialize and deserialize any local fields I add here
    private InfuserRecipe currentRecipe;

    public InfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.INFUSER_TILE, pos, state, INFUSER);

        this.items = LazyOptional.of(this::createItemHandler);
        this.energy = LazyOptional.of(this::createEnergyStorage);
        this.data = new ContainerData() {
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
    }

    private InfuserItemStackHandler createItemHandler() {
        return new InfuserItemStackHandler(TOTAL_SLOTS, this);
    }

    private SimpleGemsEnergyStorage createEnergyStorage() {
        return new SimpleGemsEnergyStorage(200000, 1000);
    }

    @Override
    public void load(CompoundTag compound) {
        final CompoundTag currentRecipeTag = compound.getCompound("currentRecipe");
        currentRecipe = (currentRecipeTag.isEmpty()) ? null : new InfuserRecipe(currentRecipeTag);
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (currentRecipe != null) {
            compound.put("currentRecipe", currentRecipe.serializeNBT());
        }
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
        InfuserBlockEntity tile = (InfuserBlockEntity) be;

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
                }

                tile.setChanged();
            }

        }
    }

    /* MARK IInventory */

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index != OUTPUT_SLOT_INDEX;
    }

    public int getEnergy() {
        return energy.map(EnergyStorage::getEnergyStored).orElse(0);
    }

    public InfuserRecipe getCurrentRecipe() {
        return currentRecipe;
    }
}
