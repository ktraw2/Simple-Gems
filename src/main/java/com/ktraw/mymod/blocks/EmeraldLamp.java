package com.ktraw.mymod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class EmeraldLamp extends Block {
    public EmeraldLamp() {
        super(Properties.create(Material.REDSTONE_LIGHT)
                        .sound(SoundType.GLASS)
                        .hardnessAndResistance(0.5f, 15f)
                        .lightValue(15));

        setRegistryName("emerald_lamp");
    }
}
