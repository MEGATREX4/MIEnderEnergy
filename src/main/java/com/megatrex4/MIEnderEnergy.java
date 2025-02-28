package com.megatrex4;

import com.megatrex4.block.entity.SolarPanelBlockEntity;
import com.megatrex4.block.entity.WirelessControllerBlockEntity;
import com.megatrex4.block.entity.WirelessOutletBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import com.megatrex4.registry.BlockRegistry;
import com.megatrex4.registry.ItemGroupRegistry;
import com.megatrex4.registry.ItemRegistry;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MIEnderEnergy implements ModInitializer {
	public static final String MOD_ID = "mienderenergy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		BlockEntityRegistry.registerBlockEntities();
		BlockRegistry.BlockRegistry();
		ItemGroupRegistry.ItemGroupRegistry();
		WirelessControllerBlockEntity.init();
		WirelessOutletBlockEntity.init();
		ItemRegistry.ItemRegistry();
		SolarPanelBlockEntity.init();

		LOGGER.info("Hello Fabric world!");
	}
}