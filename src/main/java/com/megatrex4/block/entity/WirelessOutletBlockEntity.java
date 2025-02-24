package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
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
        return false; // This block only extracts energy
    }

    @Override
    public long insert(long l, TransactionContext transactionContext) {
        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return uuid != null;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);
        long currentEnergy = GlobalEnergyStorage.getEnergy(uuid);
        long canExtract = Math.min(MAX_EXTRACT, Math.min(maxAmount, currentEnergy));

        if (canExtract > 0) {
            GlobalEnergyStorage.removeEnergy(uuid, canExtract);
            return canExtract;
        }

        return 0L;
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

    @Override
    public boolean canConnect(CableTier cableTier) {
        return true;
    }

    @Override
    public boolean canConnect(String cableTier) {
        return MIEnergyStorage.super.canConnect(cableTier);
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

    public void serverTick() {
        if (uuid == null) return;

        // Transfer energy to nearby blocks (your existing logic)
        transferEnergyToNearbyBlocks();
    }

    public static final BlockEntityTicker<WirelessOutletBlockEntity> WIRELESS_OUTLET_TICKER =
            (world, pos, state, blockEntity) -> {
                if (blockEntity != null) {
                    blockEntity.serverTick();
                }
            };


    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(
            BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    // Method to get the ticker (you can use this when registering the block entity)
    public static BlockEntityTicker<WirelessOutletBlockEntity> getTicker() {
        return checkType(BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY, BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY, WIRELESS_OUTLET_TICKER);
    }


    private void transferEnergyToNearbyBlocks() {
        World world = getWorld();
        BlockPos pos = getPos();

        for (BlockPos adjacentPos : BlockPos.iterate(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
            if (adjacentPos.equals(pos)) continue;  // Skip the current block

            BlockEntity adjacentBlockEntity = world.getBlockEntity(adjacentPos);
            if (adjacentBlockEntity instanceof MIEnergyStorage adjacentStorage) {
                // Extract energy from this outlet and transfer to the adjacent block
                long energyToTransfer = extract(MAX_EXTRACT, null);  // Extract from this block
                if (energyToTransfer > 0) {
                    // Attempt to insert energy into the adjacent block (if it supports insertion)
                    long inserted = adjacentStorage.insert(energyToTransfer, null);
                    if (inserted > 0) {
                        // Successfully transferred energy
                        GlobalEnergyStorage.removeEnergy(uuid, inserted);
                    }
                }
            }
        }
    }



}
