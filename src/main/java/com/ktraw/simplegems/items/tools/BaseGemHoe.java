package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

public class BaseGemHoe extends HoeItem {

    private static final float ATTACK_SPEED_MOFIFIER = 0.0f;

    public BaseGemHoe(Tier itemTier, Rarity rarity, String registryNamePrefix) {
        super(itemTier, ((int) -itemTier.getAttackDamageBonus()), ATTACK_SPEED_MOFIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryNamePrefix + "gem_hoe");
    }

    public BaseGemHoe(Tier itemTier, Rarity rarity) {
        this(itemTier, rarity, "");
    }
}
