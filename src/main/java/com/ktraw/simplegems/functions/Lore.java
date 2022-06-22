package com.ktraw.simplegems.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.ktraw.simplegems.registry.Items;
import com.ktraw.simplegems.util.JSONHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetLoreFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Lore extends SetLoreFunction {

    private static ArrayList<Component> fallbackLore = new ArrayList<>();
    static {
        fallbackLore.add(Component.literal("Error"));
    }

    private final boolean replace;

    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        ArrayList<Component> generatorLore = new ArrayList<>();
        if (tag != null) {
            try {
                // Get access to the lore field so we can change it
                Field loreField = SetLoreFunction.class.getDeclaredField("lore");
                loreField.setAccessible(true);

                // Get the amount of energy and add it to lore if there is energy
                int energy = tag.getCompound("energy").getInt("energy");
                if (energy > 0) {
                    generatorLore.add(Component.literal("Energy: " + energy + " FE"));
                }

                // Get the inventory and process it to see if lore is needed
                ListTag inventory = tag.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);
                if (!inventory.isEmpty()) {
                    int size = inventory.size();
                    // Make sure the item is Charged Emerald Dust
                    if (size == 1 && inventory.getCompound(0).getString("id").equals(ForgeRegistries.ITEMS.getKey(Items.CHARGED_EMERALD_DUST.get()).toString())) {
                        generatorLore.add(Component.literal("Dust: " + inventory.getCompound(0).getByte("Count")));
                    }
                    else {
                        // Iterate through inventory
                        generatorLore.add(Component.literal("Contents:"));
                        for (int i = 0; i < size; i++) {
                            CompoundTag current = inventory.getCompound(i);
                            generatorLore.add(Component.literal(" - " + current.getByte("Count") + " x " + (new ResourceLocation(current.getString("id"))).getPath()));
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
            ItemStack result = super.run(stack, context);

            // Add colors
            CompoundTag resultDisplayTag = result.getTagElement("display");
            ListTag loreList = resultDisplayTag.getList("Lore", Tag.TAG_STRING);
            for (int i = 0; i < loreList.size(); i++) {
                String lore = loreList.getString(i);
                loreList.set(i, StringTag.valueOf(lore.substring(0, lore.length() - 1) + ",\"color\":\"green\",\"italic\":false}"));
            }

            return result;
        }
        else {
            return stack;
        }
    }

    public Lore(LootItemCondition[] p_i51220_1_, boolean replace) {
        super(p_i51220_1_, replace, new ArrayList<Component>(), LootContext.EntityTarget.THIS);

        this.replace = replace;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<Lore> {

        @Override
        public void serialize(JsonObject object, Lore functionClazz, JsonSerializationContext serializationContext) {
            super.serialize(object, functionClazz, serializationContext);
            object.addProperty("replace", functionClazz.replace);
        }

        @Override
        public Lore deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            boolean flag = JSONHelper.getBooleanOrDefault("replace", object, false);
            return new Lore(conditionsIn, flag);
        }
    }

}
