package com.ktraw.simplegems.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class AmethystOre extends OreBlock {
    public AmethystOre() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(4f, 20f)
                .requiresCorrectToolForDrops()/*
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3)*/);

        setRegistryName("amethyst_ore");
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader reader, BlockPos pos, int fortune, int silktouch) {
        return (silktouch == 0) ? (Mth.nextInt(RANDOM, 4, 8)) : 0;
    }
}
