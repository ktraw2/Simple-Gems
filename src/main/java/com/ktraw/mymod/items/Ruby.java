package com.ktraw.mymod.items;

import com.ktraw.mymod.MyMod;
import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ruby extends Item {
    public Ruby() {
        super(new Item.Properties()
                  .maxStackSize(64)
                  .group(ModSetup.getSetup().getCreativeTab()));
        setRegistryName("ruby");
    }

    @Override
    public boolean isBeaconPayment(ItemStack stack) {
        return true;
    }
}
