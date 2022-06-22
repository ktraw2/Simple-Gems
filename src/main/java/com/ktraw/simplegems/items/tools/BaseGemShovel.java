package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemShovel extends ShovelItem {

    private static final float ATTACK_DAMAGE_MODIFIER = 1.5f;
    private static final float ATTACK_SPEED_MODIFIER = -3.0f;

    public static RegistryObject<BaseGemShovel> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_shovel", () -> new BaseGemShovel(itemTier, rarity));
    }

    public static RegistryObject<BaseGemShovel> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemShovel(Tier itemTier, Rarity rarity) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
    }
}
