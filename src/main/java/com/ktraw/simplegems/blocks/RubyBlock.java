package com.ktraw.simplegems.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class RubyBlock extends Block {
    public RubyBlock() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.METAL)
                .strength(5f, 30f)
                .requiresCorrectToolForDrops()/*
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)*/);

        setRegistryName("ruby_block");
    }
}
