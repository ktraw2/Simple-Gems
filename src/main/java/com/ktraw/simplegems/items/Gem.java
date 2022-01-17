package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Gem extends Item {
    public Gem() {
        super(new Item.Properties()
                .stacksTo(64)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(Rarity.RARE));
        setRegistryName("gem");
    }
}
