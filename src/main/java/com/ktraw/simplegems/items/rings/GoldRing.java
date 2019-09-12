package com.ktraw.simplegems.items.rings;

import com.ktraw.simplegems.SimpleGems;
import net.minecraft.item.Item;

public class GoldRing extends Item {
    public GoldRing() {
        super(new Properties()
                    .group(SimpleGems.setup.getCreativeTab())
                    .maxStackSize(1));

        setRegistryName("gold_ring");
    }
}
