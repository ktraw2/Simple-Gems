package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;

public class GemPickaxe extends PickaxeItem {
    public GemPickaxe() {
        super(GemItemTier.getTier(), 1, -2.8f, new Properties()
                                                                            .stacksTo(1)
                                                                            .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(Rarity.RARE));
        setRegistryName("gem_pickaxe");
    }
}
