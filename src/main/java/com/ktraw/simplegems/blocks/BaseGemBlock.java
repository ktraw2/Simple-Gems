package com.ktraw.simplegems.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class BaseGemBlock extends Block {
    public BaseGemBlock() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.METAL)
                .strength(5f, 30f)
                .requiresCorrectToolForDrops());
    }

    public BaseGemBlock(Material material, float hardness, float resistance) {
        super(Properties.of(material)
                .sound(SoundType.METAL)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops());
    }
}
