package com.megatrex4.wirelessEnergy;

import com.megatrex4.MIEnderEnergy;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomBlockEntities {
    public static BlockEntityType<WirelessNodeBlockEntity> WIRELESS_NODE;
    public static BlockEntityType<WirelessControllerBlockEntity> WIRELESS_CONTROLLER;

    public static void registerBlockEntities() {
        WIRELESS_NODE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_node"),
                BlockEntityType.Builder.create(WirelessNodeBlockEntity::new, CustomBlocks.WIRELESS_NODE_BLOCK).build(null)
        );

        WIRELESS_CONTROLLER = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller"),
                BlockEntityType.Builder.create(WirelessControllerBlockEntity::new, CustomBlocks.WIRELESS_CONTROLLER_BLOCK).build(null)
        );
    }
}
