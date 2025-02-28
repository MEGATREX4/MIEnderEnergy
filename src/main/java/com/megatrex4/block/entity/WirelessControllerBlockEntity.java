package com.megatrex4.block.entity;

import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.energy.CableTier;
import java.util.UUID;

public class WirelessControllerBlockEntity extends BlockEntity implements MIEnergyStorage {
    private UUID uuid;
    private static final long MAX_ENERGY = MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY;
    private static final long MAX_INSERT = MIEnderEnergyConfig.SERVER.MAX_INSERT;
    private static final long MAX_EXTRACT = 0;

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
        return GlobalEnergyStorage.getEnergy(uuid);
    }

    public void setStoredEnergy(long energy) {
        GlobalEnergyStorage.setEnergy(uuid, energy);
    }

    @Override
    public long getCapacity() {
        return energyStorage.getCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return 0L;
    }

    public void tick() {
        if (this.world == null || this.world.isClient)
            return;

        if (uuid != null) {
            for (Direction direction : Direction.values()) {
                EnergyStorage storage = EnergyStorage.SIDED.find(this.world, this.pos.offset(direction), direction.getOpposite());

                if (storage == null || !storage.supportsExtraction())
                    continue;

                long energyInNetwork = GlobalEnergyStorage.getEnergy(uuid);
                long maxNetworkCapacity = MAX_ENERGY;
                long freeSpaceInNetwork = maxNetworkCapacity - energyInNetwork;

                long availableEnergy = storage.getAmount();
                long maxExtractable = MAX_INSERT;
                long toExtract = Math.min(maxExtractable, availableEnergy);
                toExtract = Math.min(toExtract, freeSpaceInNetwork);

                if (toExtract > 0) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        long extracted;
                        try (Transaction simulateTransaction = transaction.openNested()) {
                            extracted = storage.extract(toExtract, simulateTransaction);
                        }

//                        System.out.println("Extracted from adjacent storage: " + extracted);
                        GlobalEnergyStorage.addEnergy(uuid, extracted);
                        storage.extract(extracted, transaction);

//                        System.out.println("Added energy to network: " + extracted);
//                        System.out.println("Energy stored in network: " + GlobalEnergyStorage.getEnergy(uuid));

                        transaction.commit();
                    }
                }
            }
        }
    }


    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return 0L;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putUuid("ControllerUUID", uuid);
        nbt.putLong("Energy", GlobalEnergyStorage.getEnergy(uuid));
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
        GlobalEnergyStorage.setEnergy(uuid, energy);
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
}
