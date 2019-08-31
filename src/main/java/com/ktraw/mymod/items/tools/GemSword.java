package com.ktraw.mymod.items.tools;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;

public class GemSword extends SwordItem {
    public GemSword () {
        super(GemItemTier.getTier(), 5, -2.4f, new Properties()
                                                                        .maxStackSize(1)
                                                                        .group(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_sword");
    }
}
