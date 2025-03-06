package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.EnergyApi;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.block.energy.EnderEnergyStorageUtil;
import com.megatrex4.block.energy.GlobalEnergyStorage;
import com.megatrex4.registry.BlockEntityRegistry;
import com.megatrex4.registry.BlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.UUID;

public class WirelessOutletBlockEntity extends PowerAcceptorBlockEntity implements EnergyComponentHolder, MIEnergyStorage  {
    private UUID uuid;

    private static final long MAX_ENERGY = MIEnderEnergyConfig.SERVER.MAX_NETWORK_ENERGY;
    private static final long MAX_EXTRACT = MIEnderEnergyConfig.SERVER.MAX_EXTRACT;



    public WirelessOutletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY, pos, state);
    }


    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public long getAmount() {
        if (uuid != null) {
            return GlobalEnergyStorage.getEnergy(uuid);
        }
        return 0;
    }

    @Override
    public long getCapacity() {
        return MAX_ENERGY;
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public long insert(long l, TransactionContext transactionContext) {
        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return true;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (uuid == null || maxAmount <= 0) {
            return 0;
        }

        long extracted = Math.min(maxAmount, getStored());
        try (Transaction nestedTransaction = Transaction.openNested(transaction)) {
            setStored(getStored() - extracted);
            nestedTransaction.commit();
            return extracted;
        }
    }


    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity2) {
        super.tick(world, pos, state, blockEntity2);
        if (world != null && !world.isClient) {
            // Sync with GlobalEnergyStorage if stored energy has changed
            if (this.getStored() != GlobalEnergyStorage.getEnergy(uuid)) {
                this.setStored(GlobalEnergyStorage.getEnergy(uuid));
            }

            if (this.getStored() > 0L) {
                if (this.isActive(RedstoneConfiguration.POWER_IO)) {
                    Direction[] var5 = Direction.values();
                    int var6 = var5.length;

                    for (int var7 = 0; var7 < var6; ++var7) {
                        Direction side = var5[var7];
                        EnergyStorageUtil.move(this.getSideEnergyStorage(side), (EnergyStorage) EnergyStorage.SIDED.find(world, pos.offset(side), side.getOpposite()), Long.MAX_VALUE, (TransactionContext) null);
                    }

                    this.powerChange = this.getStored() - this.powerLastTick;
                    this.powerLastTick = this.getStored();
                }
            }
        }
    }


    public long getMaxStoredPower() {
        return this.getBaseMaxPower() + this.extraPowerStorage;
    }


    @Override
    public long getFreeSpace(){
        return GlobalEnergyStorage.freeSpace(uuid);
    }

    @Override
    public void addEnergy(long amount) {
        GlobalEnergyStorage.addEnergy(uuid, amount);
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


    public static void registerEnergyStorage() {
//        EnergyStorage.SIDED.registerForBlockEntities(
//                (blockEntity, direction) -> blockEntity instanceof WirelessOutletBlockEntity ? (WirelessOutletBlockEntity) blockEntity : null,
//                BlockEntityRegistry.WIRELESS_OUTLET_BLOCK_ENTITY
//        );
    }

    public static void init() {
        registerEnergyStorage();
    }

    @Override
    public boolean canConnect(CableTier cableTier) {
        return true;
    }

    @Override
    public EnergyAccess getEnergyComponent() {
        return null;
    }
}
