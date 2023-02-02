package com.ktraw.simplegems.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class RubyOre extends DropExperienceBlock {

    public static String getRegistryName(boolean deepslate) {
        return (deepslate ? "deepslate_" : "") + "ruby_ore";
    }

    public RubyOre(boolean deepslate) {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength((deepslate ? 1.5f : 0.0f) + 3f, 15f)
                .requiresCorrectToolForDrops());
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return (silkTouchLevel == 0) ? (Mth.nextInt(randomSource, 3, 7)) : 0;
    }
}
