package com.megatrex4.block.entity;

import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.api.EnergyStorage;

import java.util.UUID;

public class WirelessReceiverBlockEntity extends PowerAcceptorBlockEntity implements EnergyStorage {

    private UUID uuid;
    private static final long MAX_ENERGY = MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY;
    private static final long MAX_EXTRACT = MIEnderEnergyConfig.SERVER.MAX_EXTRACT;

    public WirelessReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_RECEIVER_BLOCK_ENTITY, pos, state);
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
        return this;
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public long insert(long maxAmount, TransactionContext transactionContext) {
        long toInsert = Math.min(maxAmount, MAX_ENERGY - getStored());
        if (toInsert > 0) {
            transactionContext.addCloseCallback((ignored, result) -> {
                if (result.wasCommitted()) setStored(getStored() + toInsert);
            });
            return toInsert;
        } else {
            return 0L;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public long extract(long maxAmount, TransactionContext transactionContext) {
        return 0L;
    }

    @Override
    public long getAmount() {
        return getStored();
    }

    @Override
    public long getCapacity() {
        return getMaxStoredPower();
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public long getFreeSpace() {
        return GlobalEnergyStorage.freeSpace(uuid);
    }

    @Override
    public long getStored() {
        return GlobalEnergyStorage.getEnergy(uuid);
    }

    @Override
    public void setStored(long amount) {
        GlobalEnergyStorage.setEnergy(uuid, amount);
    }

    @Override
    public long getBaseMaxPower() {
        return MAX_ENERGY;
    }

    @Override
    public long getBaseMaxOutput() {
        return MAX_EXTRACT;
    }

    @Override
    public long getBaseMaxInput() {
        return 0;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        NbtCompound data = new NbtCompound();
        if (uuid != null) {
            data.putLong("energy", getStored());
            nbt.putUuid("ControllerUUID", uuid);
        }
        nbt.put("PowerAcceptor", data);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("ControllerUUID")) {
            uuid = nbt.getUuid("ControllerUUID");
            if (uuid != null) {
                long energy = GlobalEnergyStorage.getEnergy(uuid);
                setStored(energy);
            }
        }
    }

    public static void init() {
    }
}
