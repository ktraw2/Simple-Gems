package com.ktraw.simplegems.items;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class BaseItem extends Item {
    public BaseItem(String registryName) {
        super(new Item.Properties()
                .stacksTo(64)
                .tab(ModSetup.getSetup().getCreativeTab()));
        setRegistryName(registryName);
    }

    public BaseItem(String registryName, Rarity rarity) {
        super(new Item.Properties()
                .stacksTo(64)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
        setRegistryName(registryName);
    }

    public BaseItem(String registryName, int stacksTo) {
        super(new Properties()
                .tab(SimpleGems.setup.getCreativeTab())
                .stacksTo(stacksTo));

        setRegistryName(registryName);
    }
}
