package com.ktraw.simplegems.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class AmethystBlock extends Block {
    public AmethystBlock() {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(6f, 40f)
                .requiresCorrectToolForDrops()/*
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3)*/);

        setRegistryName("amethyst_block");
    }
}
