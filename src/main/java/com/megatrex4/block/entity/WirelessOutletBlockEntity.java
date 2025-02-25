package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import com.megatrex4.util.EnergyUtils;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.UUID;

public class WirelessOutletBlockEntity extends BlockEntity implements MIEnergyStorage {
    private UUID uuid;

    private static final long MAX_ENERGY = 1000000L;
    private static final long MAX_EXTRACT = 1000L;

    private final SimpleEnergyStorage energyStorage;


    public WirelessOutletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY, pos, state);
        this.energyStorage = new SimpleEnergyStorage(MAX_ENERGY, 0, MAX_EXTRACT);
    }


    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public long getAmount() {
        return uuid != null ? GlobalEnergyStorage.getEnergy(uuid) : 0;
    }

    @Override
    public long getCapacity() {
        return energyStorage.getCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        return energyStorage.supportsInsertion();
    }

    @Override
    public long insert(long l, TransactionContext transactionContext) {
        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return energyStorage.supportsExtraction();
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);
        long currentEnergy = GlobalEnergyStorage.getEnergy(uuid);  // Get energy of the current block

        // Directions to check (top, bottom, left, right, front, back)
        Direction[] directions = new Direction[] {
                Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };

        long totalExtracted = 0L;
        long[] energyTransferredInDirection = new long[directions.length];

        // Try to transfer energy to each direction separately
        for (int i = 0; i < directions.length; i++) {
            Direction direction = directions[i];

            // Get the adjacent energy storage using the new API
            EnergyStorage maybeStorage = EnergyStorage.SIDED.find(world, this.getPos().offset(direction), direction.getOpposite());

            if (maybeStorage != null) {
                long currentEnergyInAdjacent = maybeStorage.getAmount();
                long maxCapacityInAdjacent = maybeStorage.getCapacity();

                // Available space in adjacent block
                long availableSpaceInAdjacent = maxCapacityInAdjacent - currentEnergyInAdjacent;
                long energyToTransfer = Math.min(MAX_EXTRACT, Math.min(availableSpaceInAdjacent, currentEnergy));

                // Check if there is energy to transfer and if adjacent block has space
                if (energyToTransfer > 0 && (availableSpaceInAdjacent == maxCapacityInAdjacent) && (currentEnergy <= 0 || currentEnergyInAdjacent <= 0)) {
                    // Transfer the energy to the adjacent block
                    maybeStorage.insert(energyToTransfer, transaction);

                    // Update the current energy storage in Wireless Outlet for this direction
                    GlobalEnergyStorage.removeEnergy(uuid, energyToTransfer);

                    // Update the total amount of energy transferred
                    energyTransferredInDirection[i] = energyToTransfer;
                    totalExtracted += energyToTransfer;
                }
            }
        }

        // Return the total energy extracted (or transferred)
        return totalExtracted;
    }









    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (uuid != null){
            nbt.putUuid("ControllerUUID", uuid);
            nbt.putLong("Energy", GlobalEnergyStorage.getEnergy(uuid));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ControllerUUID")) {
            uuid = nbt.getUuid("ControllerUUID");
            long energy = nbt.getLong("Energy");
            GlobalEnergyStorage.setEnergy(uuid, energy);
        }
    }

    public static void registerEnergyStorage() {
        EnergyStorage.SIDED.registerForBlockEntities(
                (blockEntity, direction) -> blockEntity instanceof WirelessOutletBlockEntity ? (WirelessOutletBlockEntity) blockEntity : null,
                BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY
        );
    }

    public static void init() {
        registerEnergyStorage();
    }

    @Override
    public boolean canConnect(CableTier cableTier) {
        return true;
    }

    @Override
    public boolean canConnect(String cableTier) {
        return MIEnergyStorage.super.canConnect(cableTier);
    }
}
