package com.ktraw.simplegems.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class BaseItem extends Item {
    public BaseItem() {
        super(new Item.Properties()
                .stacksTo(64));
    }

    public BaseItem(final Rarity rarity) {
        super(new Item.Properties()
                .stacksTo(64)
                .rarity(rarity));
    }

    public BaseItem(final int stacksTo) {
        super(new Properties()
                .stacksTo(stacksTo));
    }
}
