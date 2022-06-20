package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

public class BaseGemAxe extends AxeItem {

    private static final float ATTACK_DAMAGE_MODIFIER = 7.0f;
    private static final float ATTACK_SPEED_MODIFIER = -3.0f;

    public BaseGemAxe(Tier itemTier, Rarity rarity, String registryNamePrefix) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryNamePrefix + "gem_axe");
    }

    public BaseGemAxe(Tier itemTier, Rarity rarity) {
        this(itemTier, rarity, "");
    }
}
