package com.ktraw.simplegems.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class GemBlock extends Block {
    public GemBlock() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(7f, 45f)
                .requiresCorrectToolForDrops()/*
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(4)*/);

        setRegistryName("gem_block");
    }
}
