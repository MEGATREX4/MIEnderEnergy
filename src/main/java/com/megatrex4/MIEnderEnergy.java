package com.megatrex4;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.megatrex4.wirelessEnergy.CustomBlockEntities.registerBlockEntities;
import static com.megatrex4.wirelessEnergy.CustomBlocks.*;

public class MIEnderEnergy implements ModInitializer {
	public static final String MOD_ID = "mienderenergy";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		registerContent();
		registerBlockEntities();
		registerItemGroup();

		LOGGER.info("Hello Fabric world!");
	}
}