package com.megatrex4.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import com.megatrex4.EnderBlockEntity; // Import EnderBlockEntity
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity; // Import EnergyStorageBlockEntity
import reborncore.common.powerSystem.RcEnergyTier;

import java.util.ArrayList;

public class WirelessControllerBlockEntity extends EnergyStorageBlockEntity {
    private int connectedBlocks = 0;
    private final ArrayList<WirelessNetwork> countedNetworks = new ArrayList<>();

    // Energy variables
    private int energyStorage = 0;
    private int maxEnergyStorage = 100000; // Example capacity

    public WirelessControllerBlockEntity(BlockPos pos, BlockState state) {
        super(EnderBlockEntity.WIRELESS_CONTROLLER, pos, state, "wireless_controller", 1, null, RcEnergyTier.INSANE, 100000); // Use EnergyStorageBlockEntity constructor
        this.energyStorage = 0;
        this.maxEnergyStorage = 100000; // Adjust based on requirements
    }

    private void checkNetwork() {
        this.countedNetworks.clear();
        this.connectedBlocks = 0;
        Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            BlockEntity adjacent = this.world.getBlockEntity(this.pos.offset(direction));
            if (adjacent instanceof WirelessNodeBlockEntity) {
                WirelessNetwork network = ((WirelessNodeBlockEntity) adjacent).getNetwork();
                if (network != null && !this.countedNetworks.contains(network)) {
                    this.connectedBlocks += network.getNodes().size();
                    this.countedNetworks.add(network);
                    network.setMaster(this);
                    break;
                }
            }
        }
    }

    private void setMaxStorage() {
        this.maxEnergyStorage = (this.connectedBlocks + 1) * 100000; // Example calculation
        if (this.maxEnergyStorage < 0 || this.maxEnergyStorage > Integer.MAX_VALUE) {
            this.maxEnergyStorage = Integer.MAX_VALUE;
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state) {
        super.tick(world, pos, state, this); // Correct the superclass call with the additional parameter
        if (!world.isClient) {
            if (world.getTime() % 20L == 0L) {
                this.checkNetwork();
            }
            this.setMaxStorage();
        }
    }

    @Override
    public long getEnergy() {  // Return type changed to long
        return energyStorage;  // This will return a long
    }

    @Override
    public void setEnergy(long energy) {  // Change parameter type to long
        this.energyStorage = (int) energy;  // Ensure to cast to int if you need to store it as an int
    }

    @Override
    public long getMaxEnergyStorage() {  // Return type changed to long
        return maxEnergyStorage;  // This should return a long
    }

    public int getConnectedBlocks() {
        return connectedBlocks;
    }
}
