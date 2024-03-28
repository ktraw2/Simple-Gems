package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.util.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.Nullable;

public class EmeraldLamp extends Block {
    public static final IntegerProperty LIGHT_LEVEL = BlockStateProperties.LEVEL;
    public static final int MAX_LEVEL = BlockStateProperties.MAX_LEVEL_15;

    private final boolean inverted;

    public static String getRegistryName(boolean inverted) {
        return (inverted ? "inverted_" : "" ) + "emerald_lamp";
    }

    public EmeraldLamp(boolean inverted) {
        super(Material.BUILDABLE_GLASS.properties()
                .sound(SoundType.GLASS)
                .strength(0.5f, 15f)
                .lightLevel(value -> inverted ? MAX_LEVEL : 0));

        this.registerDefaultState(this.defaultBlockState().setValue(LIGHT_LEVEL, 0));
        this.inverted = inverted;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(LIGHT_LEVEL, context.getLevel().getBestNeighborSignal(context.getClickedPos()));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        final int stateLevel = state.getValue(LIGHT_LEVEL);
        return inverted ? MAX_LEVEL - stateLevel : stateLevel;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            int power = worldIn.getBestNeighborSignal(pos);
            if (power != state.getValue(LIGHT_LEVEL)) {
                worldIn.setBlock(pos, state.setValue(LIGHT_LEVEL, power), 2);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL);
    }
}
