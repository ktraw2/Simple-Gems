package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.SimpleGems;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class SimpleGemsContainerBlock extends Block implements EntityBlock {

    private final Supplier<RegistryObject<BlockEntityType<? extends BlockEntity>>> blockEntityTypeWrapper;
    private final BlockEntityTicker<? extends BlockEntity> blockEntityTicker;
    private final BiFunction<BlockPos, BlockState, ? extends BlockEntity> blockEntityFactory;

    public SimpleGemsContainerBlock(Supplier<RegistryObject<BlockEntityType<? extends BlockEntity>>> blockEntityTypeWrapper, BlockEntityTicker<? extends BlockEntity> blockEntityTicker, BiFunction<BlockPos, BlockState, ? extends BlockEntity> blockEntityFactory) {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(5f, 15f));
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));

        this.blockEntityTypeWrapper = blockEntityTypeWrapper;
        this.blockEntityTicker = blockEntityTicker;
        this.blockEntityFactory = blockEntityFactory;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) { // TODO: onBlockPlacedBy?
        if (placer != null) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.HORIZONTAL_FACING, SimpleGems.getFacingFromEntity(pos, placer, true)), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof MenuProvider) {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
            }

            return InteractionResult.SUCCESS;
        }
        else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFactory.apply(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == blockEntityTypeWrapper.get().get() ? ((BlockEntityTicker<T>) blockEntityTicker) : null; // evil cast
    }
}
