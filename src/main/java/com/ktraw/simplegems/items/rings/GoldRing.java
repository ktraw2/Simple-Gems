package com.ktraw.simplegems.items.rings;

import com.ktraw.simplegems.SimpleGems;
import net.minecraft.world.item.Item;

public class GoldRing extends Item {
    public GoldRing() {
        super(new Properties()
                    .tab(SimpleGems.setup.getCreativeTab())
                    .stacksTo(1));

        setRegistryName("gold_ring");
    }
}
