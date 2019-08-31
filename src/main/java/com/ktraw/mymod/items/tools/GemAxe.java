package com.ktraw.mymod.items.tools;

import com.ktraw.mymod.setup.ModSetup;
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
