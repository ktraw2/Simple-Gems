package com.ktraw.simplegems.items.tools;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemAxe extends AxeItem {

    private static final float ATTACK_DAMAGE_MODIFIER = 7.0f;
    private static final float ATTACK_SPEED_MODIFIER = -3.0f;

    public static RegistryObject<BaseGemAxe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_axe", () -> new BaseGemAxe(itemTier, rarity));
    }

    public static RegistryObject<BaseGemAxe> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemAxe(Tier itemTier, Rarity rarity) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .rarity(rarity));
    }
}
