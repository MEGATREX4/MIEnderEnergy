package com.megatrex4;

import com.megatrex4.blockentity.WirelessControllerBlockEntity;
import com.megatrex4.blockentity.WirelessNodeBlockEntity;
import com.megatrex4.registry.BlockRegistry;
import net.minecraft.block.entity.BlockEntityType;

public class EnderBlockEntity {
    public static final BlockEntityType<WirelessControllerBlockEntity> WIRELESS_CONTROLLER = BlockEntityType.Builder.create(WirelessControllerBlockEntity::new, BlockRegistry.WIRELESS_CONTROLLER_BLOCK).build(null);
    public static final BlockEntityType<WirelessNodeBlockEntity> WIRELESS_NODE = BlockEntityType.Builder.create(WirelessNodeBlockEntity::new, BlockRegistry.WIRELESS_NODE_BLOCK).build(null);
}
