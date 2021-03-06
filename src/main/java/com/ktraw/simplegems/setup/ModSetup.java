package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.ktraw.simplegems.functions.CopyNbt;
import com.ktraw.simplegems.functions.Lore;
import com.ktraw.simplegems.items.ModItems;
import com.ktraw.simplegems.world.OreGeneration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.potion.*;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Method;

public final class ModSetup {
    private static ModSetup setup = new ModSetup();
    private ItemGroup creativeTab;

    private ModSetup() {
        creativeTab = new ItemGroup("simplegems") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModBlocks.EMERALD_LAMP);
            }
        };
    }

    public static ModSetup getSetup() {
        return setup;
    }

    public ItemGroup getCreativeTab() {
        return creativeTab;
    }

    public void init() {
        OreGeneration.setupOreGeneration();

        Registry.register(Registry.field_239694_aZ_, new ResourceLocation(SimpleGems.MODID,"lore"), new LootFunctionType(new Lore.Serializer()));
        Registry.register(Registry.field_239694_aZ_, new ResourceLocation(SimpleGems.MODID, "copy_nbt"), new LootFunctionType(new CopyNbt.Serializer()));

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
