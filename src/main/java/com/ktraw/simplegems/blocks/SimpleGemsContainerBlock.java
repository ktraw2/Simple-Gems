package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.util.DirectionUtil;
import com.ktraw.simplegems.util.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class SimpleGemsContainerBlock extends Block implements EntityBlock {

    private final Supplier<RegistryObject<BlockEntityType<? extends BlockEntity>>> blockEntityTypeWrapper;
    private final BlockEntityTicker<? extends BlockEntity> blockEntityTicker;
    private final BiFunction<BlockPos, BlockState, ? extends BlockEntity> blockEntityFactory;

    public SimpleGemsContainerBlock(
            final Supplier<RegistryObject<BlockEntityType<? extends BlockEntity>>> blockEntityTypeWrapper,
            final BlockEntityTicker<? extends BlockEntity> blockEntityTicker,
            final BiFunction<BlockPos, BlockState, ? extends BlockEntity> blockEntityFactory
    ) {
        super(Material.METAL.properties()
                .sound(SoundType.METAL)
                .strength(5f, 15f));
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));

        this.blockEntityTypeWrapper = blockEntityTypeWrapper;
        this.blockEntityTicker = blockEntityTicker;
        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    public void setPlacedBy(
            @Nonnull final Level worldIn,
            @Nonnull final BlockPos pos,
            @Nonnull final BlockState state,
            @Nullable final LivingEntity placer,
            @Nonnull final ItemStack stack
    ) { // TODO: onBlockPlacedBy?
        if (placer != null) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, DirectionUtil.getFacingFromEntity(pos, placer, true)), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Nonnull
    @Override
    public InteractionResult use(
            @Nonnull final BlockState state,
            final Level worldIn,
            @Nonnull final BlockPos pos,
            @Nonnull final Player player,
            @Nonnull final InteractionHand handIn,
            @Nonnull final BlockHitResult hit
    ) {
        if (!worldIn.isClientSide) {
            final BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof MenuProvider) {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(
            @Nonnull final BlockPos pos,
            @Nonnull final BlockState state
    ) {
        return blockEntityFactory.apply(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @Nonnull final Level world,
            @Nonnull final BlockState state,
            @Nonnull final BlockEntityType<T> type
    ) {
        return type == blockEntityTypeWrapper.get().get() ? ((BlockEntityTicker<T>) blockEntityTicker) : null; // evil cast
    }
}
