package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.UUID;

public class WirelessOutletBlockEntity extends BlockEntity implements MIEnergyStorage {
    private UUID uuid;

    private static final long MAX_ENERGY = MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY;
    private static final long MAX_EXTRACT = MIEnderEnergyConfig.SERVER.MAX_EXTRACT;

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
        return 0;
    }


    public void tick() {
        if (this.world == null || this.world.isClient)
            return;

        if (uuid != null && GlobalEnergyStorage.getEnergy(uuid) > 0) {
            for (Direction direction : Direction.values()) {
                // Try Modern Industrialization's MIEnergyStorage first
                MIEnergyStorage miStorage = EnergyApi.SIDED.find(this.world, this.pos.offset(direction), direction.getOpposite());

                // If MI energy storage is found and can connect, use it
                if (miStorage != null && miStorage.canConnect((CableTier) null) && miStorage.supportsInsertion()) {
                    transferEnergyToStorage(miStorage);
                    continue;
                }

                // Otherwise, try Team Reborn Energy (TRE) system
                EnergyStorage treStorage = EnergyStorage.SIDED.find(this.world, this.pos.offset(direction), direction.getOpposite());

                if (treStorage != null && treStorage.supportsInsertion()) {
                    transferEnergyToStorage(treStorage);
                }
            }
        }
    }

    private void transferEnergyToStorage(EnergyStorage storage) {
        long currentEnergyInAdjacent = storage.getAmount();
        long maxCapacityInAdjacent = storage.getCapacity();
        long freeSpaceInAdjacent = maxCapacityInAdjacent - currentEnergyInAdjacent;

        long extracted = Math.min(MAX_EXTRACT, GlobalEnergyStorage.getEnergy(uuid));
        extracted = Math.min(extracted, freeSpaceInAdjacent);

        if (freeSpaceInAdjacent > 0) {
            try (Transaction transaction = Transaction.openOuter()) {
                long insertable;
                try (Transaction simulateTransaction = transaction.openNested()) {
                    insertable = storage.insert(extracted, simulateTransaction);
                }

                long inserted = storage.insert(insertable, transaction);
                GlobalEnergyStorage.removeEnergy(uuid, inserted);
                transaction.commit();
            }
        }
    }






    private void update() {
        markDirty();
        if(world != null)
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
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
