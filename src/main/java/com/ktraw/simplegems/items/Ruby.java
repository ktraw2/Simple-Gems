package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;

public class Ruby extends Item {
    public Ruby() {
        super(new Item.Properties()
                  .stacksTo(64)
                  .tab(ModSetup.getSetup().getCreativeTab()));
        setRegistryName("ruby");
    }
}
