package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.item.Item.Properties;

public class BaseGemHoe extends HoeItem {

    private static final float ATTACK_SPEED_MOFIFIER = 0.0f;

    public static RegistryObject<BaseGemHoe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_hoe", () -> new BaseGemHoe(itemTier, rarity));
    }

    public static RegistryObject<BaseGemHoe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemHoe(Tier itemTier, Rarity rarity) {
        super(itemTier, ((int) -itemTier.getAttackDamageBonus()), ATTACK_SPEED_MOFIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
    }
}
