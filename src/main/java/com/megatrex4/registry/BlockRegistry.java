package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.block.WirelessControllerBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    public static final Block WIRELESS_CONTROLLER_BLOCK = registerBlock("wireless_controller_block",
            new WirelessControllerBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).strength(4.0f).sounds(BlockSoundGroup.METAL)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MIEnderEnergy.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Item item = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, new Identifier(MIEnderEnergy.MOD_ID, name), item);
    }

    public static void BlockRegistry(){
        MIEnderEnergy.LOGGER.info("Registering Blocks for " + MIEnderEnergy.MOD_ID);
    }

}
