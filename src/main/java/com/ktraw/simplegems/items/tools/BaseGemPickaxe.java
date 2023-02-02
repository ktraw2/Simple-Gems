package com.ktraw.simplegems.items.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemPickaxe extends PickaxeItem {

    private static final int ATTACK_DAMAGE_MODIFIER = 1;
    private static final float ATTACK_SPEED_MODIFIER = -2.8f;

    public static RegistryObject<BaseGemPickaxe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_pickaxe", () -> new BaseGemPickaxe(itemTier, rarity));
    }

    public static RegistryObject<BaseGemPickaxe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemPickaxe(Tier itemTier, Rarity rarity) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .rarity(rarity));
    }
}
