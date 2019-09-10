package com.ktraw.simplegems.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.SetLore;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Lore extends SetLore {


    private static ArrayList<ITextComponent> fallbackLore = new ArrayList<>();
    static {
        fallbackLore.add(new StringTextComponent("Error"));
    }

    private final boolean replace;

    @Override
    public ItemStack doApply(ItemStack stack, LootContext context) {
        CompoundNBT tag = stack.getChildTag("BlockEntityTag");
        ArrayList<ITextComponent> generatorLore = new ArrayList<>();
        if (tag != null) {
            try {
                // Get access to the lore field so we can change it
                Field loreField = SetLore.class.getDeclaredField("lore");
                loreField.setAccessible(true);

                // Get the amount of energy and add it to lore if there is energy
                int energy = tag.getCompound("energy").getInt("energy");
                if (energy > 0) {
                    generatorLore.add(new StringTextComponent("Energy: " + energy + " FE"));
                }

                // Get the inventory and process it to see if lore is needed
                ListNBT inventory = tag.getCompound("inventory").getList("Items", Constants.NBT.TAG_COMPOUND);
                if (!inventory.isEmpty()) {
                    int size = inventory.size();
                    // Make sure the item is Charged Emerald Dust
                    if (size == 1 && inventory.getCompound(0).getString("id").equals(ModItems.CHARGED_EMERALD_DUST.getRegistryName().toString())) {
                        generatorLore.add(new StringTextComponent("Dust: " + inventory.getCompound(0).getByte("Count")));
                    }
                    else {
                        // Iterate through inventory
                        generatorLore.add(new StringTextComponent("Contents:"));
                        for (int i = 0; i < size; i++) {
                            CompoundNBT current = inventory.getCompound(i);
                            generatorLore.add(new StringTextComponent(" - " + current.getByte("Count") + " x " + ForgeRegistries.ITEMS.getValue(new ResourceLocation(current.getString("id"))).getRegistryName().getPath()));
                        }
                    }
                }

                loreField.set(this, generatorLore);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Only put the lore if there is lore to put
        if (generatorLore.size() > 0) {
            ItemStack result = super.doApply(stack, context);

            // Add colors
            CompoundNBT resultDisplayTag = result.getChildTag("display");
            ListNBT loreList = resultDisplayTag.getList("Lore", Constants.NBT.TAG_STRING);
            for (int i = 0; i < loreList.size(); i++) {
                String lore = loreList.getString(i);
                loreList.set(i, new StringNBT(lore.substring(0, lore.length() - 1) + ",\"color\":\"green\",\"italic\":false}"));
            }

            return result;
        }
        else {
            return stack;
        }
    }

    public Lore(ILootCondition[] p_i51220_1_, boolean replace) {
        super(p_i51220_1_, replace, new ArrayList<ITextComponent>(), LootContext.EntityTarget.THIS);

        this.replace = replace;
    }

    public static class Serializer extends LootFunction.Serializer<Lore> {
        public Serializer() {
            super(new ResourceLocation(SimpleGems.MODID,"lore"), Lore.class);
        }

        public void serialize(JsonObject object, Lore functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
            object.addProperty("replace", functionClazz.replace);
        }

        public Lore deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            boolean flag = JSONUtils.getBoolean(object, "replace", false);
            return new Lore(conditionsIn, flag);
        }
    }

}
