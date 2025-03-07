package com.megatrex4.block.entity;

import aztech.modern_industrialization.api.energy.CableTier;
import aztech.modern_industrialization.api.energy.MIEnergyStorage;
import aztech.modern_industrialization.api.machine.component.EnergyAccess;
import aztech.modern_industrialization.api.machine.holder.EnergyComponentHolder;
import aztech.modern_industrialization.machines.multiblocks.HatchBlockEntity;
import com.megatrex4.MIEnderEnergyConfig;
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

        World currentWorld = this.world;
        boolean isRainy = currentWorld.isRaining();
        boolean isNight = currentWorld.getTimeOfDay() >= 12500L && currentWorld.getTimeOfDay() <= 23500L;

        // Adjusted timeFactor based on your requirement:
        float timeFactor = 0.0F;
        long timeOfDay = currentWorld.getTimeOfDay() % 24000L;

        if (timeOfDay >= 0L && timeOfDay < 6000L) {  // Morning: 0 to 6000
            timeFactor = 0.5F + (timeOfDay / 12000.0F); // Smooth transition from 0.5 to 1.0
        } else if (timeOfDay >= 6000L && timeOfDay < 18000L) {  // Day: 6000 to 18000
            timeFactor = 1.0F; // Maximum at midday
        } else if (timeOfDay >= 18000L && timeOfDay < 24000L) {  // Evening: 18000 to 24000
            timeFactor = 0.5F - ((timeOfDay - 18000L) / 12000.0F); // Smooth transition from 0.5 back to 0
        }

        float PI = MathHelper.PI;

        long generation = (long) ((getBaseMaxOutput() / PI) / PI);

        if (isNight) {
            if (cableTier.compareTo(CableTier.HV) < 0) {
                return;
            }

            energyToAdd = (long) (generation / 2 / PI);

            if (isRainy) {
                energyToAdd /= 2;
            }
        } else {
            if (isRainy && cableTier.compareTo(CableTier.HV) < 0) {
                return;
            }
            energyToAdd = (long) (generation * timeFactor);
        }

        // Apply world-specific multiplier
        String worldKey = currentWorld.getRegistryKey().getValue().toString();
        if (MIEnderEnergyConfig.SERVER.WORLD_MULTIPLIERS.containsKey(worldKey)) {
            System.out.println("world multiplier found, energyToAdd *= " + MIEnderEnergyConfig.SERVER.WORLD_MULTIPLIERS.get(worldKey) + " : " + energyToAdd);
            energyToAdd *= MIEnderEnergyConfig.SERVER.WORLD_MULTIPLIERS.get(worldKey);
        } else {
            energyToAdd *= 1;
            System.out.println("world multiplier not found");
        }

        // Add energy if it's greater than 0
        if (energyToAdd > 0) {
            this.addEnergy(energyToAdd);
            markDirty();
        }
    }





}
