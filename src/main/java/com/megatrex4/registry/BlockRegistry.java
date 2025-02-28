package com.megatrex4.registry;

import aztech.modern_industrialization.api.energy.CableTier;
import com.megatrex4.MIEnderEnergy;
import com.megatrex4.block.SolarPanelBlockBuilder;
import com.megatrex4.block.WirelessControllerBlock;
import com.megatrex4.block.WirelessOutletBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import com.megatrex4.MIEnderEnergyConfig;

import static com.megatrex4.MIEnderEnergy.MOD_ID;

public class BlockRegistry {

    public static final Block WIRELESS_CONTROLLER_BLOCK = registerBlock("wireless_controller_block",
            new WirelessControllerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(3.0f).sounds(BlockSoundGroup.METAL)));

    public static final Block WIRELESS_OUTLET_BLOCK = registerBlock("wireless_outlet_block",
            new WirelessOutletBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(3.0f).sounds(BlockSoundGroup.METAL)));



    public static final Block SOLAR_PANEL_LV_BLOCK = registerBlock("solar_panel_lv_block",
            new SolarPanelBlockBuilder(MIEnderEnergyConfig.SERVER.LV_CAPACITY, MIEnderEnergyConfig.SERVER.LV_EXTRACTION_RATE, CableTier.LV).build());

    public static final Block SOLAR_PANEL_MV_BLOCK = registerBlock("solar_panel_mv_block",
            new SolarPanelBlockBuilder(MIEnderEnergyConfig.SERVER.MV_CAPACITY, MIEnderEnergyConfig.SERVER.MV_EXTRACTION_RATE, CableTier.MV).build());

    public static final Block SOLAR_PANEL_HV_BLOCK = registerBlock("solar_panel_hv_block",
            new SolarPanelBlockBuilder(MIEnderEnergyConfig.SERVER.HV_CAPACITY, MIEnderEnergyConfig.SERVER.HV_EXTRACTION_RATE, CableTier.HV).build());

    public static final Block SOLAR_PANEL_EV_BLOCK = registerBlock("solar_panel_ev_block",
            new SolarPanelBlockBuilder(MIEnderEnergyConfig.SERVER.EV_CAPACITY, MIEnderEnergyConfig.SERVER.EV_EXTRACTION_RATE, CableTier.EV).build());

    public static final Block SOLAR_PANEL_IV_BLOCK = registerBlock("solar_panel_iv_block",
            new SolarPanelBlockBuilder(MIEnderEnergyConfig.SERVER.IV_CAPACITY, MIEnderEnergyConfig.SERVER.IV_EXTRACTION_RATE, CableTier.SUPERCONDUCTOR).build());


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MOD_ID,name.toLowerCase()), block);
    }

    private static void registerBlockItem(String name, Block block){
        Item item = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, new Identifier(MIEnderEnergy.MOD_ID, name), item);
    }

    public static void BlockRegistry(){
        MIEnderEnergy.LOGGER.info("Registering Blocks for " + MIEnderEnergy.MOD_ID);
    }
}
