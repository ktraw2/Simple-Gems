package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.functions.CopyNbt;
import com.ktraw.simplegems.functions.Lore;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.world.OreGeneration;
import lombok.Getter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import java.lang.reflect.Method;

@Getter
public final class ModSetup {
    @Getter
    private static ModSetup setup = new ModSetup();
    private CreativeModeTab creativeTab;

    private ModSetup() {
        creativeTab = new CreativeModeTab("simplegems") {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModBlocks.EMERALD_LAMP);
            }
        };
    }

    public void init() {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(SimpleGems.MODID,"lore"), new LootItemFunctionType(new Lore.Serializer()));
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(SimpleGems.MODID, "copy_nbt"), new LootItemFunctionType(new CopyNbt.Serializer()));

        try {
            Method addMix = PotionBrewing.class.getDeclaredMethod("addMix", Potion.class, Item.class, Potion.class);
            addMix.setAccessible(true);
            addMix.invoke(null, Potions.AWKWARD, ModItems.RUBY, Potions.LUCK);
        }
        catch (Exception e) {
            System.err.println("Error adding custom potions.");
            e.printStackTrace();
        }
    }
}
