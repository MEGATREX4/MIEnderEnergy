package com.megatrex4.blockentity;

import com.megatrex4.EnderBlockEntity;
import com.megatrex4.MIEnderEnergy;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static void modBlockEntitiesRegister() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MIEnderEnergy.MOD_ID, "wireless_controller"), EnderBlockEntity.WIRELESS_CONTROLLER);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MIEnderEnergy.MOD_ID, "wireless_node"), EnderBlockEntity.WIRELESS_NODE);
    }
}

