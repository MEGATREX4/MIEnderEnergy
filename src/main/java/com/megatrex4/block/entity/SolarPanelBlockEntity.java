package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class SolarPanelBlockEntity extends PowerAcceptorBlockEntity implements EnergyComponentHolder, MIEnergyStorage {

    private final long capacity;
    private final long extractRate;
    private final CableTier cableTier;


    public SolarPanelBlockEntity(BlockPos pos, BlockState state, long capacity, long extractionRate, CableTier tier) {
        super(BlockEntityRegistry.SOLAR_PANEL_BLOCK_ENTITY, pos, state);
        this.capacity = capacity;
        this.extractRate = extractionRate;
        this.cableTier = tier;
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
        long extracted = Math.min(maxAmount, getStored());
        try (Transaction nestedTransaction = Transaction.openNested(transaction)) {
            setStored(getStored() - extracted);
            nestedTransaction.commit();
            return extracted;
        }
    }

    @Override
    public long getAmount() {
        return this.getStored();
    }

    public long getCapacity() {
        return capacity;
    }

    public long getExtractRate() {
        return extractRate;
    }

    @Override
    public long getBaseMaxPower() {
        return capacity;
    }

    @Override
    public long getBaseMaxOutput() {
        return extractRate;
    }

    @Override
    public long getBaseMaxInput() {
        return 0;
    }

    public CableTier getMITier() {
        return cableTier;
    }

    @Override
    public EnergyAccess getEnergyComponent() {
        return null;
    }

    @Override
    public boolean canConnect(CableTier cableTier) {
        return cableTier.equals(this.getMITier());
    }


    public static void registerEnergyStorage() {
//        EnergyStorage.SIDED.registerForBlockEntities(
//                (blockEntity, direction) -> blockEntity instanceof SolarPanelBlockEntity ? (SolarPanelBlockEntity) blockEntity : null,
//                BlockEntityRegistry.SOLAR_PANEL_BLOCK_ENTITY
//        );
    }

    public static void init() {
        registerEnergyStorage();
    }

    public void tick() {
        if (this.world == null || this.world.isClient) return;

        long energyToAdd = 0;

        // Check if it's raining or night-time
        boolean isNightOrRainy = world.isRaining() || world.getTimeOfDay() % 24000L >= 13000L || world.getTimeOfDay() % 24000L <= 1000L;

        // In the middle of the day, we generate more energy
        float timeFactor = MathHelper.clamp((float) (world.getTimeOfDay() % 24000L) / 12000.0F, 0.0F, 1.0F);

        // Check if the tier is HV or lower
        if (cableTier.compareTo(CableTier.HV) <= 0) {
            if (isNightOrRainy) {
                return;
            }

            energyToAdd = Math.min(getBaseMaxOutput(), getMaxStoredPower() - getStored());
        } else {
            if (isNightOrRainy) {
                energyToAdd = Math.min(getBaseMaxOutput(), getMaxStoredPower() - getStored()) / 2;
            } else {
                energyToAdd = (long) (getBaseMaxOutput() * timeFactor);
            }
        }

        if (energyToAdd > 0) {
            this.addEnergy(energyToAdd);
            markDirty();
        }
    }



}
