package com.megatrex4.registry;

import com.megatrex4.MIEnderEnergy;
import com.megatrex4.item.ConfiguratorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.megatrex4.MIEnderEnergy.MOD_ID;

public class ItemRegistry {

    public static final Item CONFIGURATOR = new ConfiguratorItem(new Item.Settings().maxCount(1));

    public static Item registerItems(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID,name.toLowerCase()), item);
    }

    public static void ItemRegistry() {
        MIEnderEnergy.LOGGER.info("Registering Items for " + MOD_ID);
        registerItems("configurator", CONFIGURATOR);
    }
}
