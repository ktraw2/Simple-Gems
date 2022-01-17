package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;

public class GemSword extends SwordItem {
    public GemSword () {
        super(GemItemTier.getTier(), 5, -2.4f, new Properties()
                                                                        .stacksTo(1)
                                                                        .tab(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_sword");
    }
}
