package com.ktraw.simplegems.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.Nonnull;

public class CopyNbt extends LootItemConditionalFunction {

    public CopyNbt(final LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    @Override
    protected ItemStack run(
            @Nonnull final ItemStack stack,
            final LootContext context
    ) {
        final CompoundTag compoundNBT = context.getParam(LootContextParams.BLOCK_ENTITY).serializeNBT();
        if (compoundNBT != null) {
            CompoundTag stackTag = null;

            final boolean processing = compoundNBT.getBoolean("processing");
            if (processing) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.putBoolean("processing", processing);
            }

            final int timer = compoundNBT.getInt("timer");
            if (timer != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.putInt("timer", timer);
            }

            final CompoundTag energy = compoundNBT.getCompound("energy");
            final int energyValue = energy.getInt("energy");
            if (energyValue != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.put("energy", energy);
            }

            final CompoundTag inventory = compoundNBT.getCompound("inventory");

            final ListTag inventoryItems = inventory.getList("Items", Tag.TAG_COMPOUND);
            if (!inventoryItems.isEmpty()) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.put("inventory", inventory);
            }

            final CompoundTag currentRecipe = compoundNBT.getCompound("currentRecipe");
            if (!currentRecipe.isEmpty()) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.put("currentRecipe", currentRecipe);
            }
        }

        return stack;
    }

    @Nonnull
    @Override
    public LootItemFunctionType getType() {
        return new LootItemFunctionType(new Serializer());
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyNbt> {
        @Nonnull
        @Override
        public CopyNbt deserialize(
                @Nonnull final JsonObject object,
                @Nonnull final JsonDeserializationContext deserializationContext,
                @Nonnull final LootItemCondition[] conditionsIn
        ) {
            return new CopyNbt(conditionsIn);
        }
    }
}
