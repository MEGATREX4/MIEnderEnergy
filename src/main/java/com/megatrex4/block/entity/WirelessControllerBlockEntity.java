package com.megatrex4.block.entity;

import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.energy.CableTier;
import java.util.UUID;

public class WirelessControllerBlockEntity extends BlockEntity implements MIEnergyStorage {
    private UUID uuid;
    private static final long MAX_ENERGY = 1000000L;
    private static final long MAX_INSERT = 1000L;
    private static final long MAX_EXTRACT = 1000L;

    private final SimpleEnergyStorage energyStorage;

    public WirelessControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_CONTROLLER_BLOCK_ENTITY, pos, state);
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        this.energyStorage = new SimpleEnergyStorage(MAX_ENERGY, MAX_INSERT, MAX_EXTRACT);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public long getAmount() {
        return energyStorage.getAmount();
    }

    public long getStoredEnergy() {
        return GlobalEnergyStorage.getEnergy(uuid);
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
    public long insert(long amount, TransactionContext transactionContext) {
        return energyStorage.insert(amount, transactionContext); // Delegate to SimpleEnergyStorage
    }

    @Override
    public boolean supportsExtraction() {
        return energyStorage.supportsExtraction();
    }

    @Override
    public long extract(long amount, TransactionContext transactionContext) {
        return energyStorage.extract(amount, transactionContext); // Delegate to SimpleEnergyStorage
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putUuid("ControllerUUID", uuid);
        nbt.putLong("Energy", GlobalEnergyStorage.getEnergy(uuid)); // Save energy from GlobalEnergyStorage
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ControllerUUID")) {
            uuid = nbt.getUuid("ControllerUUID");
        } else {
            uuid = UUID.randomUUID();
        }
        long energy = nbt.getLong("Energy");
        // Set energy in GlobalEnergyStorage instead of using SimpleEnergyStorage.
        GlobalEnergyStorage.setEnergy(uuid, energy);  // Use GlobalEnergyStorage to set energy
    }

    public static void registerEnergyStorage() {
        EnergyStorage.SIDED.registerForBlockEntities(
                (blockEntity, direction) -> blockEntity instanceof WirelessControllerBlockEntity ? (WirelessControllerBlockEntity) blockEntity : null,
                BlockEntityRegistry.WIRELESS_CONTROLLER_BLOCK_ENTITY
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
