package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;

public class GemShovel extends ShovelItem {
    public GemShovel() {
        super(GemItemTier.getTier(), 1.5f, -3f, new Properties()
                                                                            .maxStackSize(1)
                                                                            .group(ModSetup.getSetup().getCreativeTab())
                                                                            .rarity(Rarity.RARE));
        setRegistryName("gem_shovel");
    }
}
