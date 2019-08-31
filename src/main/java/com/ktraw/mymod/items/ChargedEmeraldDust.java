package com.ktraw.mymod.items;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ChargedEmeraldDust extends Item {
    public ChargedEmeraldDust() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModSetup.getSetup().getCreativeTab()));
        setRegistryName("charged_emerald_dust");
    }
}
