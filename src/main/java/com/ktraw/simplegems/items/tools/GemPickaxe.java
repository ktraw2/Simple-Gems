package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
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
