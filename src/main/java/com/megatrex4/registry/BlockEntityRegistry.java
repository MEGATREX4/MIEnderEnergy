package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {

    public static BlockEntityType<WirelessControllerBlockEntity> WIRELESS_CONTROLLER_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        WIRELESS_CONTROLLER_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller_block_entity"),
                FabricBlockEntityTypeBuilder.create(WirelessControllerBlockEntity::new, BlockRegistry.WIRELESS_CONTROLLER_BLOCK).build()
        );

        MIEnderEnergy.LOGGER.info("Registered Block Entities");
    }
}
