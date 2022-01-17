package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;

public class Amethyst extends Item {
    public Amethyst() {
        super(new Properties()
                .stacksTo(64)
                .tab(ModSetup.getSetup().getCreativeTab()));
        setRegistryName("amethyst");
    }
}
