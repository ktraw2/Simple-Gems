package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;

public class BaseGemPickaxe extends PickaxeItem {

    private static final int ATTACK_DAMAGE_MODIFIER = 1;
    private static final float ATTACK_SPEED_MODIFIER = -2.8f;

    public BaseGemPickaxe(Tier itemTier, Rarity rarity, String registryNamePrefix) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryNamePrefix + "gem_pickaxe");
    }

    public BaseGemPickaxe(Tier itemTier, Rarity rarity) {
        this(itemTier, rarity, "");
    }
}
