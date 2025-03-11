package com.megatrex4.block;

import aztech.modern_industrialization.proxy.CommonProxy;
import com.megatrex4.block.energy.format;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import com.megatrex4.block.entity.WirelessReceiverBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import java.util.List;
import java.util.UUID;

public class WirelessReceiverBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public WirelessReceiverBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessReceiverBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessReceiverBlockEntity receiverEntity) {
                if (itemStack.hasNbt() && itemStack.getNbt().contains("ControllerUUID")) {
                    UUID uuid = itemStack.getNbt().getUuid("ControllerUUID");
                    receiverEntity.setUUID(uuid);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.literal(" "));
        if (CommonProxy.INSTANCE.hasShiftDown()) {

            String configurator = Text.translatable("item.mienderenergy.configurator").getString();
            String controller = Text.translatable("block.mienderenergy.wireless_controller_block").getString();

            String details = Text.translatable("tooltip.mienderenergy.wireless_receiver.details", controller, configurator).getString();

            format.fotmattedTooltips(tooltip, details);

        } else {
            tooltip.add(Text.translatable("tooltip.mienderenergy.more").formatted(Formatting.DARK_GRAY));
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityRegistry.WIRELESS_RECEIVER_BLOCK_ENTITY ?
                (world1, pos, state1, blockEntity) -> ((WirelessReceiverBlockEntity) blockEntity).tick(world1, pos, state1, (MachineBaseBlockEntity) blockEntity) : null;
    }
}
