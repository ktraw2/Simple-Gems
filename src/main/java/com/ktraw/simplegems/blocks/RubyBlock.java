package com.ktraw.simplegems.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class RubyBlock extends Block {
    public RubyBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(5f, 30f)
                .setRequiresTool()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .setRequiresTool());

        setRegistryName("ruby_block");
    }
}
