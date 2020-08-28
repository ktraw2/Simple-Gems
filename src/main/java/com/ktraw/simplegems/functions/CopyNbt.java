package com.ktraw.simplegems.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.ktraw.simplegems.SimpleGems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.util.Constants;


public class CopyNbt extends LootFunction {

    public CopyNbt(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        CompoundNBT compoundNBT = context.get(LootParameters.BLOCK_ENTITY).serializeNBT();
        if (compoundNBT != null) {
            CompoundNBT stackTag = null;

            boolean processing = compoundNBT.getBoolean("processing");
            if (processing) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateChildTag("BlockEntityTag");
                }
                stackTag.putBoolean("processing", processing);
            }

            int counter = compoundNBT.getInt("counter");
            if (counter != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateChildTag("BlockEntityTag");
                }
                stackTag.putInt("counter", counter);
            }

            CompoundNBT energy = compoundNBT.getCompound("energy");
            int energyValue = energy.getInt("energy");
            if (energyValue != 0) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateChildTag("BlockEntityTag");
                }
                stackTag.put("energy", energy);
            }

            CompoundNBT inventory = compoundNBT.getCompound("inventory");
            ListNBT inventoryItems = inventory.getList("Items", Constants.NBT.TAG_COMPOUND);
            if (!inventoryItems.isEmpty()) {
                if (stackTag == null) {
                    stackTag = stack.getOrCreateChildTag("BlockEntityTag");
                }
                stackTag.put("inventory", inventory);
            }
        }

        return stack;
    }

    @Override
    public LootFunctionType func_230425_b_() {
        return new LootFunctionType(new Serializer());
    }

    public static class Serializer extends LootFunction.Serializer<CopyNbt> {

        @Override
        public void func_230424_a_(JsonObject object, CopyNbt functionClazz, JsonSerializationContext serializationContext) {
            super.func_230424_a_(object, functionClazz, serializationContext);
        }

        public CopyNbt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new CopyNbt(conditionsIn);
        }
    }
}
