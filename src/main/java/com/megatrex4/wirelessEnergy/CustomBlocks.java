package com.megatrex4.wirelessEnergy;

import com.megatrex4.MIEnderEnergy;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class CustomBlocks {
    // Define your blocks
    public static final Block WIRELESS_NODE_BLOCK = new Block(AbstractBlock.Settings.create()
            .mapColor(MapColor.IRON_GRAY)
            .strength(5.0f, 6.0f)
            .sounds(BlockSoundGroup.METAL));

    public static final Block WIRELESS_CONTROLLER_BLOCK = new Block(AbstractBlock.Settings.create()
            .mapColor(MapColor.IRON_GRAY)
            .strength(5.0f, 6.0f)
            .sounds(BlockSoundGroup.METAL));

    // Define the block items
    public static final Item.Settings DEFAULT_ITEM_SETTINGS = new Item.Settings().rarity(Rarity.UNCOMMON);

    // Define a custom item group
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.wirelessEnergy"))
            .icon(() -> new ItemStack(WIRELESS_NODE_BLOCK)) // Set the icon
            .entries((displayContext, entries) -> {
                entries.add(new ItemStack(WIRELESS_NODE_BLOCK));
                entries.add(new ItemStack(WIRELESS_CONTROLLER_BLOCK));
            })
            .build();

    /**
     * Registers a block and its corresponding item.
     *
     * @param name  The registry name.
     * @param block The block to register.
     * @param itemSettings The item settings for the block's item.
     */
    private static void registerBlockWithItem(String name, Block block, Item.Settings itemSettings) {
        Identifier id = new Identifier(MIEnderEnergy.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, itemSettings));
    }

    public static void registerContent() {
        // Register blocks and their items
        registerBlockWithItem("wireless_node", WIRELESS_NODE_BLOCK, DEFAULT_ITEM_SETTINGS);
        registerBlockWithItem("wireless_controller", WIRELESS_CONTROLLER_BLOCK, DEFAULT_ITEM_SETTINGS);
    }

    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP,
                new Identifier(MIEnderEnergy.MOD_ID, "wireless_energy_group"),
                CUSTOM_ITEM_GROUP);
    }
}
