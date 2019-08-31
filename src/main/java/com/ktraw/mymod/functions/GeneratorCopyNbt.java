package com.ktraw.mymod.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.util.Constants;


public class GeneratorCopyNbt extends LootFunction {

    public GeneratorCopyNbt(ILootCondition[] conditionsIn) {
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

    public static class Serializer extends LootFunction.Serializer<GeneratorCopyNbt> {
        public Serializer() {
            super(new ResourceLocation("mymod_generator_copy_nbt"), GeneratorCopyNbt.class);
        }

        public void serialize(JsonObject object, GeneratorCopyNbt functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
        }

        public GeneratorCopyNbt deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            return new GeneratorCopyNbt(conditionsIn);
        }
    }
}
