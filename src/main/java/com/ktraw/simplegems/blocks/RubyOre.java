package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.util.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class RubyOre extends DropExperienceBlock {

    public static String getRegistryName(final boolean deepslate) {
        return (deepslate ? "deepslate_" : "") + "ruby_ore";
    }

    public RubyOre(final boolean deepslate) {
        super(Material.STONE.properties()
                .sound(deepslate ? SoundType.DEEPSLATE : SoundType.STONE)
                .strength((deepslate ? 1.5f : 0.0f) + 3f, 15f)
                .requiresCorrectToolForDrops());
    }

    @Override
    public int getExpDrop(
            @Nonnull final BlockState state,
            @Nonnull final LevelReader level,
            @Nonnull final RandomSource randomSource,
            @Nonnull final BlockPos pos,
            final int fortuneLevel,
            final int silkTouchLevel
    ) {
        return (silkTouchLevel == 0) ? (Mth.nextInt(randomSource, 3, 7)) : 0;
    }
}
