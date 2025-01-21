package com.megatrex4;

import com.megatrex4.registry.BlockRegistry;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.megatrex4.blockentity.ModBlockEntities.modBlockEntitiesRegister;

public class MIEnderEnergy implements ModInitializer {
	public static final String MOD_ID = "mienderenergy";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BlockRegistry.registerBlocks();
		ModItemGroups.registerItemGroups();
		modBlockEntitiesRegister();

		LOGGER.info("Hello Fabric world!");
	}
}