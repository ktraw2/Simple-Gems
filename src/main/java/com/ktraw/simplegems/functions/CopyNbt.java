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


public class CopyNbt extends LootItemConditionalFunction {

    public CopyNbt(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        CompoundTag compoundNBT = context.getParam(LootContextParams.BLOCK_ENTITY).serializeNBT();
        if (compoundNBT != null) {
            CompoundTag stackTag = null;

            boolean processing = compoundNBT.getBoolean("processing");
            if (processing) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.putBoolean("processing", processing);
            }

            int counter = compoundNBT.getInt("counter");
            if (counter != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.putInt("counter", counter);
            }

            CompoundTag energy = compoundNBT.getCompound("energy");
            int energyValue = energy.getInt("energy");
            if (energyValue != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.put("energy", energy);
            }

            CompoundTag inventory = compoundNBT.getCompound("inventory");

            ListTag inventoryItems = inventory.getList("Items", Tag.TAG_COMPOUND);
            if (!inventoryItems.isEmpty()) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateTagElement("BlockEntityTag");
                }
                stackTag.put("inventory", inventory);
            }
        }

        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return new LootItemFunctionType(new Serializer());
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyNbt> {
        @Override
        public CopyNbt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new CopyNbt(conditionsIn);
        }
    }
}
