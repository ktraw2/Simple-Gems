package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class BaseGemShovel extends ShovelItem {

    private static final float ATTACK_DAMAGE_MODIFIER = 1.5f;
    private static final float ATTACK_SPEED_MODIFIER = -3.0f;

    public BaseGemShovel(Tier itemTier, Rarity rarity, String registryNamePrefix) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryNamePrefix + "gem_shovel");
    }

    public BaseGemShovel(Tier itemTier, Rarity rarity) {
        this(itemTier, rarity, "");
    }
}
