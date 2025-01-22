package com.megatrex4.block;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import net.minecraft.block.BlockEntityProvider;
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

import java.util.UUID;

public class WirelessControllerBlock extends BlockWithEntity implements BlockEntityProvider {

    public WirelessControllerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        MIEnderEnergy.LOGGER.info("Create Block Entity");
        return new WirelessControllerBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity controller) {
                UUID uuid = controller.getUniqueId();
                player.sendMessage(Text.literal("UUID: " + uuid), true);
                MIEnderEnergy.LOGGER.info("UUID on use: " + uuid);
            }
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
            nbt.putString("uuid", controller.getUniqueId().toString()); // Store UUID as String
            MIEnderEnergy.LOGGER.info("Drop item with UUID on break: " + controller.getUniqueId());
            stack.setNbt(nbt);
            dropStack(world, pos, stack);
        }
        super.onBreak(world, pos, state, player); // Call the superclass method
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity controller) {
                NbtCompound nbt = new NbtCompound();
                controller.writeNbt(nbt);
                if (!world.isClient) {
                    ItemStack stack = new ItemStack(this);
                    stack.setNbt(nbt); // Save NBT data, including UUID, to the dropped item
                    dropStack(world, pos, stack);
                    MIEnderEnergy.LOGGER.info("Dropped item with UUID on block state replaced: " + controller.getUniqueId());
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
