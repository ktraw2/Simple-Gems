package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Rarity;

public class GemHoe extends HoeItem {
    public GemHoe() {
        super(GemItemTier.getTier(), 1, 0.0F, new Properties()
                                                        .stacksTo(1)
                                                        .tab(ModSetup.getSetup().getCreativeTab())
                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_hoe");
    }
}
