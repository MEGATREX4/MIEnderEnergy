package com.megatrex4.block;

import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NbtCompound;

public class WirelessControllerBlock extends BlockWithEntity {

    public WirelessControllerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessControllerBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.literal("Interacted with Wireless Controller!"), true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof WirelessControllerBlockEntity controller) {
            // Save UUID to the dropped item
            ItemStack stack = new ItemStack(this);
            NbtCompound nbt = new NbtCompound();
            nbt.putUuid("BlockEntityUUID", controller.getUniqueId());
            stack.setNbt(nbt);
            dropStack(world, pos, stack);
        }
        super.onBreak(world, pos, state, player); // Call the superclass method
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity) {
                world.removeBlockEntity(pos); // Clean up the block entity
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
