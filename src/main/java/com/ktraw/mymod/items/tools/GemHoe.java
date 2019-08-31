package com.ktraw.mymod.items.tools;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Rarity;

public class GemHoe extends HoeItem {
    public GemHoe() {
        super(GemItemTier.getTier(), 1, new Properties()
                                                        .maxStackSize(1)
                                                        .group(ModSetup.getSetup().getCreativeTab())
                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_hoe");
    }
}
