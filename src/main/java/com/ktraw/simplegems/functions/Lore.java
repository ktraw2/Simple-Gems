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

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Lore extends SetLoreFunction {
    private final boolean replace;

    @Nonnull
    @Override
    public ItemStack run(
            final ItemStack stack,
            @Nonnull final LootContext context
    ) {
        final CompoundTag tag = stack.getTagElement("BlockEntityTag");
        final ArrayList<Component> generatorLore = new ArrayList<>();
        if (tag != null) {
            try {
                // Get access to the lore field so we can change it
                final Field loreField = SetLoreFunction.class.getDeclaredField("lore");
                loreField.setAccessible(true);

                // Get the amount of energy and add it to lore if there is energy
                final int energy = tag.getCompound("energy").getInt("energy");
                if (energy > 0) {
                    generatorLore.add(Component.literal("Energy: " + energy + " FE"));
                }

                // Get the inventory and process it to see if lore is needed
                final ListTag inventory = tag.getCompound("inventory").getList("Items", Tag.TAG_COMPOUND);
                if (!inventory.isEmpty()) {
                    final int size = inventory.size();
                    // Make sure the item is Charged Emerald Dust
                    if (size == 1 && inventory.getCompound(0).getString("id").equals(ForgeRegistries.ITEMS.getKey(Items.CHARGED_EMERALD_DUST.get()).toString())) {
                        generatorLore.add(Component.literal("Dust: " + inventory.getCompound(0).getByte("Count")));
                    } else {
                        // Iterate through inventory
                        generatorLore.add(Component.literal("Contents:"));
                        for (int i = 0; i < size; i++) {
                            final CompoundTag current = inventory.getCompound(i);
                            generatorLore.add(Component.literal(" - " + current.getByte("Count") + " x " + (new ResourceLocation(current.getString("id"))).getPath()));
                        }
                    }
                }

                loreField.set(this, generatorLore);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        // Only put the lore if there is lore to put
        if (!generatorLore.isEmpty()) {
            final ItemStack result = super.run(stack, context);

            // Add colors
            final CompoundTag resultDisplayTag = result.getTagElement("display");
            final ListTag loreList = resultDisplayTag.getList("Lore", Tag.TAG_STRING);
            for (int i = 0; i < loreList.size(); i++) {
                final String lore = loreList.getString(i);
                loreList.set(i, StringTag.valueOf(lore.substring(0, lore.length() - 1) + ",\"color\":\"green\",\"italic\":false}"));
            }

            return result;
        } else {
            return stack;
        }
    }

    public Lore(
            final LootItemCondition[] conditions,
            final boolean replace
    ) {
        super(conditions, replace, new ArrayList<>(), LootContext.EntityTarget.THIS);

        this.replace = replace;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<Lore> {

        @Override
        public void serialize(
                @Nonnull final JsonObject object,
                @Nonnull final Lore functionClazz,
                @Nonnull final JsonSerializationContext serializationContext
        ) {
            super.serialize(object, functionClazz, serializationContext);
            object.addProperty("replace", functionClazz.replace);
        }

        @Nonnull
        @Override
        public Lore deserialize(
                @Nonnull final JsonObject object,
                @Nonnull final JsonDeserializationContext deserializationContext,
                @Nonnull final LootItemCondition[] conditionsIn
        ) {
            final boolean flag = JSONHelper.getBooleanOrDefault("replace", object, false);
            return new Lore(conditionsIn, flag);
        }
    }

}
