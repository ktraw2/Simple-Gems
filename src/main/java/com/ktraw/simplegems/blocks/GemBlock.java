package com.ktraw.simplegems.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class GemBlock extends Block {
    public GemBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(7f, 45f)
                .setRequiresTool()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(4));

        setRegistryName("gem_block");
    }
}
