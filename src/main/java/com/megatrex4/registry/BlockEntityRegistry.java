package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.block.SolarPanelBlock;
import com.megatrex4.block.entity.SolarPanelBlockEntity;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {

    public static BlockEntityType<WirelessControllerBlockEntity> WIRELESS_CONTROLLER_BLOCK_ENTITY;
    public static BlockEntityType<WirelessOutletBlockEntity> WIRELESS_OUTLET_BLOCK_ENTITY;

    public static BlockEntityType<SolarPanelBlockEntity> SOLAR_PANEL_BLOCK_ENTITY;


    public static void registerBlockEntities() {
        WIRELESS_CONTROLLER_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller_block_entity"),
                FabricBlockEntityTypeBuilder.create(WirelessControllerBlockEntity::new, BlockRegistry.WIRELESS_CONTROLLER_BLOCK).build()
        );

        WIRELESS_OUTLET_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_outlet_block"),
                FabricBlockEntityTypeBuilder.create(WirelessOutletBlockEntity::new, BlockRegistry.WIRELESS_OUTLET_BLOCK).build()
        );

        SOLAR_PANEL_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MIEnderEnergy.MOD_ID, "solar_panel_block_entity"),
                FabricBlockEntityTypeBuilder.create((pos, state) -> {
                                    Block block = state.getBlock();
                                    if (block instanceof SolarPanelBlock solarPanel) {
                                        return new SolarPanelBlockEntity(pos, state, solarPanel.getCapacity(), solarPanel.getExtractionRate(), solarPanel.getTier());
                                    }
                                    return null;
                                },
                                BlockRegistry.SOLAR_PANEL_LV_BLOCK,
                                BlockRegistry.SOLAR_PANEL_MV_BLOCK,
                                BlockRegistry.SOLAR_PANEL_HV_BLOCK,
                                BlockRegistry.SOLAR_PANEL_EV_BLOCK,
                                BlockRegistry.SOLAR_PANEL_IV_BLOCK
                        )
                        .build()
        );

        MIEnderEnergy.LOGGER.info("Registered Block Entities");
    }
}
