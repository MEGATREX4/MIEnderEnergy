package com.megatrex4.block.energy;

import com.megatrex4.MIEnderEnergyConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobalEnergyStorage {
    private static final Map<UUID, Long> energyStorage = new HashMap<>();

    public static long getEnergy(UUID uuid) {
        return energyStorage.getOrDefault(uuid, 0L);
    }

    public static void setEnergy(UUID uuid, long energy) {
        energyStorage.put(uuid, energy);
    }

    public static void addEnergy(UUID uuid, long amount) {
        energyStorage.put(uuid, getEnergy(uuid) + amount);
    }

    public static void removeEnergy(UUID uuid, long amount) {
        energyStorage.put(uuid, Math.max(0, getEnergy(uuid) - amount));
    }

    public static long freeSpace(UUID uuid) {
        return MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY - getEnergy(uuid);
    }
}
