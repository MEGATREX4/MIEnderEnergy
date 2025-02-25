package com.megatrex4.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

public class EnergyUtils {

    /**
     * Utility method to get the energy storage in the specified direction from the current block position.
     *
     * @param world The world instance.
     * @param pos The position of the current block.
     * @param direction The direction to check for energy storage.
     * @return EnergyStorage or null if no valid storage is found.
     */
    public static EnergyStorage getEnergyStorageInDirection(World world, BlockPos pos, Direction direction) {
        BlockPos targetPos = pos.offset(direction);
        EnergyStorage maybeStorage = EnergyStorage.SIDED.find(world, targetPos, direction.getOpposite());

        return maybeStorage;
    }
}

