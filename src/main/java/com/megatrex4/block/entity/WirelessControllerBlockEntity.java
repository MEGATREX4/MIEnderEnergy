package com.megatrex4.block.entity;

import aztech.modern_industrialization.machines.init.MachineTier;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.energy.CableTier;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class WirelessControllerBlockEntity extends BlockEntity implements MIEnergyStorage {
    private UUID uuid;
    private static final long MAX_ENERGY = 10000L;

    public WirelessControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_CONTROLLER_BLOCK_ENTITY, pos, state);
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public long getStoredEnergy() {
        return GlobalEnergyStorage.getEnergy(uuid);
    }

    public void setStoredEnergy(long energy) {
        GlobalEnergyStorage.setEnergy(uuid, energy);
    }

    @Override
    public long getAmount() {
        return getStoredEnergy();
    }

    @Override
    public long getCapacity() {
        return MAX_ENERGY;
    }

    @Override
    public boolean supportsInsertion() {
        return getStoredEnergy() < MAX_ENERGY;
    }

    @Override
    public long insert(long amount, TransactionContext transactionContext) {
        long energy = getStoredEnergy();
        long inserted = Math.min(amount, MAX_ENERGY - energy);
        setStoredEnergy(energy + inserted);
        return inserted;
    }

    @Override
    public boolean supportsExtraction() {
        return getStoredEnergy() > 0;
    }

    @Override
    public long extract(long amount, TransactionContext transactionContext) {
        long energy = getStoredEnergy();
        long extracted = Math.min(amount, energy);
        setStoredEnergy(energy - extracted);
        return extracted;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putUuid("ControllerUUID", uuid);
        nbt.putLong("Energy", getStoredEnergy());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ControllerUUID")) {
            uuid = nbt.getUuid("ControllerUUID");
        } else {
            uuid = UUID.randomUUID();
        }
        setStoredEnergy(nbt.getLong("Energy"));
    }

    @Override
    public boolean canConnect(CableTier cableTier) {
        return true;
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
}
