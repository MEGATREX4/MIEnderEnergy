package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import com.megatrex4.MIEnderEnergyConfig;
import com.megatrex4.registry.BlockEntityRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public class SolarPanelBlockEntity extends PowerAcceptorBlockEntity implements EnergyComponentHolder, MIEnergyStorage {

    private final long capacity;
    private final long extractRate;
    private final long generationRate;
    private final CableTier cableTier;


    public SolarPanelBlockEntity(BlockPos pos, BlockState state, long capacity, long extractionRate, long generationRate, CableTier tier) {
        super(BlockEntityRegistry.SOLAR_PANEL_BLOCK_ENTITY, pos, state);
        this.capacity = capacity;
        this.extractRate = extractionRate;
        this.generationRate = generationRate;
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

    public long getGenerationRate() {
        return generationRate;
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

    public static void init() {
    }

    public void tick() {
        if (this.world == null || this.world.isClient) return;

        long energyToAdd = 0;

        World currentWorld = this.world;
        boolean isRainy = currentWorld.isRaining();
        boolean isNight = currentWorld.getTimeOfDay() >= 12500L && currentWorld.getTimeOfDay() <= 23500L;

        float timeFactor = 0.0F;
        long timeOfDay = currentWorld.getTimeOfDay() % 24000L;

        if (timeOfDay >= 0L && timeOfDay < 6000L) {
            timeFactor = 0.5F + (timeOfDay / 12000.0F);
        } else if (timeOfDay >= 6000L && timeOfDay < 18000L) {
            timeFactor = 1.0F;
        } else if (timeOfDay >= 18000L && timeOfDay < 24000L) {
            timeFactor = 0.5F - ((timeOfDay - 18000L) / 12000.0F);
        }


        long generation = generationRate;

        if (isNight) {
            if (cableTier.compareTo(CableTier.HV) < 0) {
                return;
            }

            energyToAdd = generation;

            if (isRainy) {
                energyToAdd /= 2;
            }
        } else {
            if (isRainy && cableTier.compareTo(CableTier.HV) < 0) {
                return;
            }
            energyToAdd = (long) (generation * timeFactor);

            if (isRainy) {
                energyToAdd /= 2;
            }
        }

        // Apply world-specific multiplier
        String worldKey = currentWorld.getRegistryKey().getValue().toString();
        if (MIEnderEnergyConfig.SERVER.WORLD_MULTIPLIERS.containsKey(worldKey)) {
            energyToAdd *= MIEnderEnergyConfig.SERVER.WORLD_MULTIPLIERS.get(worldKey);
        } else {
            energyToAdd *= 1;
        }

        // Add energy if it's greater than 0
        if (energyToAdd > 0) {
            this.addEnergy(energyToAdd);
            markDirty();
        }
    }





}
