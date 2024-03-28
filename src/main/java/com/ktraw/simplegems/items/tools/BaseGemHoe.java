package com.ktraw.simplegems.items.tools;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemHoe extends HoeItem {

    private static final float ATTACK_SPEED_MOFIFIER = 0.0f;

    public static RegistryObject<BaseGemHoe> create(
            final DeferredRegister<Item> registry,
            final Tier itemTier,
            final Rarity rarity,
            final String registryNamePrefix
    ) {
        return registry.register(registryNamePrefix + "gem_hoe", () -> new BaseGemHoe(itemTier, rarity));
    }

    public static RegistryObject<BaseGemHoe> create(
            final DeferredRegister<Item> registry,
            final Tier itemTier,
            final Rarity rarity
    ) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemHoe(
            final Tier itemTier,
            final Rarity rarity
    ) {
        super(itemTier, ((int) -itemTier.getAttackDamageBonus()), ATTACK_SPEED_MOFIFIER, new Properties()
                .stacksTo(1)
                .rarity(rarity));
    }
}
