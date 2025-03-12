package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {

    public static final ItemGroup MIEnderEnergyGroup = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MIEnderEnergy.MOD_ID, "mienderenergy"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("mienderenergy.itemgroup.group"))
                    .icon(() -> new ItemStack(BlockRegistry.WIRELESS_CONTROLLER_BLOCK))
                    .entries((displayContext, entries) -> {
                        entries.add(BlockRegistry.WIRELESS_OUTLET_BLOCK);
                        entries.add(BlockRegistry.WIRELESS_CONTROLLER_BLOCK);
                        entries.add(BlockRegistry.WIRELESS_RECEIVER_BLOCK);
                        entries.add(ItemRegistry.CONFIGURATOR);
                        entries.add(ItemRegistry.ENDER_UPGRADE);

                        entries.add(BlockRegistry.SOLAR_PANEL_LV_BLOCK);
                        entries.add(BlockRegistry.SOLAR_PANEL_MV_BLOCK);
                        entries.add(BlockRegistry.SOLAR_PANEL_HV_BLOCK);
                        entries.add(BlockRegistry.SOLAR_PANEL_EV_BLOCK);
                        entries.add(BlockRegistry.SOLAR_PANEL_IV_BLOCK);
                    })
                    .build());

    public static void ItemGroupRegistry() {
        MIEnderEnergy.LOGGER.info("Registering Item Groups for " + MIEnderEnergy.MOD_ID);
    }
}
