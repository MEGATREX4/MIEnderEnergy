package com.megatrex4.registry;

import aztech.modern_industrialization.MIItem;
import aztech.modern_industrialization.compat.kubejs.KubeJSProxy;
import aztech.modern_industrialization.machines.components.UpgradeComponent;
import com.megatrex4.MIEnderEnergy;
import com.megatrex4.item.ConfiguratorItem;
import com.megatrex4.item.EnderUpgradeItem;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.megatrex4.MIEnderEnergy.MOD_ID;

public class ItemRegistry {

    public static final Item CONFIGURATOR = new ConfiguratorItem(new Item.Settings().maxCount(1));
    public static final Item ENDER_UPGRADE = new EnderUpgradeItem(new Item.Settings().maxCount(1));

    public static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name.toLowerCase()), item);
    }

    public static void ItemRegistry() {
        MIEnderEnergy.LOGGER.info("Registering Items for " + MOD_ID);
        registerItems("configurator", CONFIGURATOR);
        registerItems("ender_upgrade", ENDER_UPGRADE);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            UpgradeComponent.registerUpgrade(new Identifier(MOD_ID, "ender_upgrade"), 9000L);
        });


    }
}
