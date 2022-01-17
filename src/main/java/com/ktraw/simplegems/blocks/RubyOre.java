package com.ktraw.simplegems.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class RubyOre extends OreBlock {
    public RubyOre(boolean deepslate) {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength((deepslate ? 1.5f : 0.0f) + 3f, 15f)
                .requiresCorrectToolForDrops()/*
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)*/);

        setRegistryName((deepslate ? "deepslate_" : "") + "ruby_ore");
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader reader, BlockPos pos, int fortune, int silktouch) {
        return (silktouch == 0) ? (Mth.nextInt(RANDOM, 3, 7)) : 0;
    }
}
