package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.WirelessControllerBlock;
import com.megatrex4.WirelessNodeBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    // Define the blocks
    public static final WirelessControllerBlock WIRELESS_CONTROLLER_BLOCK = new WirelessControllerBlock();
    public static final WirelessNodeBlock WIRELESS_NODE_BLOCK = new WirelessNodeBlock();

    // Register the blocks
    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller_block"), WIRELESS_CONTROLLER_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(MIEnderEnergy.MOD_ID, "wireless_node_block"), WIRELESS_NODE_BLOCK);

        // Register the block items
        Registry.register(Registries.ITEM, new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller_block"), new BlockItem(WIRELESS_CONTROLLER_BLOCK, new Item.Settings()));
        Registry.register(Registries.ITEM, new Identifier(MIEnderEnergy.MOD_ID, "wireless_node_block"), new BlockItem(WIRELESS_NODE_BLOCK, new Item.Settings()));
    }
}
