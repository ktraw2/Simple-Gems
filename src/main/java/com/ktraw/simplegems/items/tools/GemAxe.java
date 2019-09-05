package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Rarity;

public class GemAxe extends AxeItem {
    public GemAxe() {
        super(GemItemTier.getTier(), 7, -3f, new Properties()
                                                                            .maxStackSize(1)
                                                                            .group(ModSetup.getSetup().getCreativeTab())
                                                                            .rarity(Rarity.RARE));
        setRegistryName("gem_axe");
    }
}
