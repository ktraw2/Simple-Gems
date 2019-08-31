package com.ktraw.mymod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class AmethystOre extends OreBlock {
    public AmethystOre() {
        super(Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(4f, 20f)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(3));

        setRegistryName("amethyst_ore");
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return (silktouch == 0) ? (MathHelper.nextInt(RANDOM, 4, 8)) : 0;
    }
}
