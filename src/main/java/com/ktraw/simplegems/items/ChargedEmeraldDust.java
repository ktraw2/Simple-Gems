package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;

public class ChargedEmeraldDust extends Item {
    public ChargedEmeraldDust() {
        super(new Item.Properties()
                .stacksTo(64)
                .tab(ModSetup.getSetup().getCreativeTab()));
    }
}
