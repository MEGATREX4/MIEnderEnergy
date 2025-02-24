package com.megatrex4.block;

import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class WirelessControllerBlock extends BlockWithEntity {

    public WirelessControllerBlock(Settings settings) {
        super(settings);
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
                }
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessControllerBlockEntity wirelessEntity) {
                UUID uuid = wirelessEntity.getUUID();
                long energy = wirelessEntity.getStoredEnergy();
                player.sendMessage(Text.literal("Wireless Controller UUID: " + uuid + " | Energy: " + energy + " FE"), false);
            }
        }
        return ActionResult.SUCCESS;
    }
}
