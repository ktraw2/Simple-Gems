package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BaseGemSword extends SwordItem {

    private static final int ATTACK_DAMAGE_MODIFIER = 5;
    private static final float ATTACK_SPEED_MODIFIER = -2.4f;

    public BaseGemSword(Tier itemTier, Rarity rarity, String registryNamePrefix) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryNamePrefix + "gem_sword");
    }

    public BaseGemSword(Tier itemTier, Rarity rarity) {
        this(itemTier, rarity, "");
    }
}
