package com.megatrex4;

import com.megatrex4.registry.BlockRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    // Define the item group using Fabric's API
    public static final ItemGroup MOD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MIEnderEnergy.MOD_ID, "mienderenergy"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("group.mienderenergy.title")) // Group name
                    .icon(() -> new ItemStack(BlockRegistry.WIRELESS_CONTROLLER_BLOCK)) // Icon for the item group
                    .entries((displayContext, itemEntries) -> {
                        // Add the blocks to the item group
                        itemEntries.add(BlockRegistry.WIRELESS_CONTROLLER_BLOCK);
                        itemEntries.add(BlockRegistry.WIRELESS_NODE_BLOCK);
                    })
                    .build()
    );

    public static void registerItemGroups() {
        // Item group registration is done in the above line, no need to call anything extra here
    }
}
