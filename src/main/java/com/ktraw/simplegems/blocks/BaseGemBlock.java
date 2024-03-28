package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.util.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class BaseGemBlock extends Block {
    public BaseGemBlock() {
        super(Material.STONE.properties()
                .sound(SoundType.METAL)
                .strength(5f, 30f)
                .requiresCorrectToolForDrops());
    }

    public BaseGemBlock(
            final Material material,
            final float hardness,
            final float resistance
    ) {
        super(material.properties()
                .sound(SoundType.METAL)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops());
    }
}
