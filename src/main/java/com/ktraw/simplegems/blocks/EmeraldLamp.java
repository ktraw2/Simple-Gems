package com.ktraw.simplegems.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

public class EmeraldLamp extends Block {
    public static final IntegerProperty LIGHT_LEVEL = BlockStateProperties.LEVEL_0_15;

    public EmeraldLamp() {
        super(Properties.create(Material.REDSTONE_LIGHT)
                .sound(SoundType.GLASS)
                .hardnessAndResistance(0.5f, 15f)
                .setLightLevel(value -> 0));

        this.setDefaultState(this.getDefaultState().with(LIGHT_LEVEL, 0));
        setRegistryName("emerald_lamp");
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(LIGHT_LEVEL, context.getWorld().getRedstonePowerFromNeighbors(context.getPos()));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(LIGHT_LEVEL);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            int power = worldIn.getRedstonePowerFromNeighbors(pos);
            if (power != state.get(LIGHT_LEVEL)) {
                worldIn.setBlockState(pos, state.with(LIGHT_LEVEL, power), 2);
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }
}
