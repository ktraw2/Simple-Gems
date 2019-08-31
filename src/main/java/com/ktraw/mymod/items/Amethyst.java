package com.ktraw.mymod.items;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Amethyst extends Item {
    public Amethyst() {
        super(new Properties()
                .maxStackSize(64)
                .group(ModSetup.getSetup().getCreativeTab()));
        setRegistryName("amethyst");
    }

    @Override
    public boolean isBeaconPayment(ItemStack stack) {
        return true;
    }
}
