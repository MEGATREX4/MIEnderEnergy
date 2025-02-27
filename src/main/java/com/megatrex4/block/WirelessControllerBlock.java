package com.megatrex4.block;

import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WirelessControllerBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public WirelessControllerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessControllerBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity wirelessEntity) {
                ItemStack stack = new ItemStack(this);
                NbtCompound nbt = new NbtCompound();
                nbt.putUuid("ControllerUUID", wirelessEntity.getUUID());
                stack.setNbt(nbt);

                dropStack(world, pos, stack);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity wirelessEntity) {
                if (itemStack.hasNbt() && itemStack.getNbt().contains("ControllerUUID")) {
                    UUID uuid = itemStack.getNbt().getUuid("ControllerUUID");
                    wirelessEntity.setUUID(uuid);
                    long storedEnergy = GlobalEnergyStorage.getEnergy(uuid);
                    wirelessEntity.setStoredEnergy(storedEnergy);
                }
            }

            world.setBlockState(pos, state, 3);
            world.updateListeners(pos, state, state, 3);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING); // Example if it's a directional block
    }


    @Override
    public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return state;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityRegistry.WIRELESS_CONTROLLER_BLOCK_ENTITY ?
                (world1, pos, state1, blockEntity) -> ((WirelessControllerBlockEntity) blockEntity).tick() : null;
    }

}
