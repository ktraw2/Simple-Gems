package com.ktraw.mymod.items.tools;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;

public class GemPickaxe extends PickaxeItem {
    public GemPickaxe() {
        super(GemItemTier.getTier(), 1, -2.8f, new Properties()
                                                                            .maxStackSize(1)
                                                                            .group(ModSetup.getSetup().getCreativeTab())
                .rarity(Rarity.RARE));
        setRegistryName("gem_pickaxe");
    }
}
