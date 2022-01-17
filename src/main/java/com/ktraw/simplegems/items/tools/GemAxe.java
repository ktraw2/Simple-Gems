package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Rarity;

public class GemAxe extends AxeItem {
    public GemAxe() {
        super(GemItemTier.getTier(), 7, -3f, new Properties()
                                                                            .stacksTo(1)
                                                                            .tab(ModSetup.getSetup().getCreativeTab())
                                                                            .rarity(Rarity.RARE));
        setRegistryName("gem_axe");
    }
}
