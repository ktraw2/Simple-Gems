package com.ktraw.mymod.items;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

public class Gem extends Item {
    public Gem() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModSetup.getSetup().getCreativeTab())
                .rarity(Rarity.RARE));
        setRegistryName("gem");
    }

    @Override
    public boolean isBeaconPayment(ItemStack stack) {
        return true;
    }
}
