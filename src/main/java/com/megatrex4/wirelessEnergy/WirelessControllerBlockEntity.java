package com.megatrex4.wirelessEnergy;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;

import java.util.HashSet;
import java.util.Set;

public class WirelessControllerBlockEntity extends EnergyStorageBlockEntity {
    private String networkId = "";

    private final Set<BlockPos> connectedNodes = new HashSet<>();
    private final int baseStorage = 1000000;
    private final int storagePerNode = 50000;
    private final int baseInput = 1000;
    private final int baseOutput = 1000;
    private final int ioPerNode = 100;

    private int range = 16;

    public WirelessControllerBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.WIRELESS_CONTROLLER, pos, state, "Wireless Controller", 2, CustomBlocks.WIRELESS_CONTROLLER_BLOCK, RcEnergyTier.MEDIUM, 1000000);
    }

    public void updateConnectedNodes() {
        connectedNodes.clear();
        BlockPos.iterate(this.pos.add(-range, -range, -range), this.pos.add(range, range, range)).forEach(pos -> {
            BlockEntity be = this.world.getBlockEntity(pos);
            if (be instanceof WirelessNodeBlockEntity node && node.getNetworkId().equals(this.getNetworkId())) {
                connectedNodes.add(pos);
            }
        });
        updateEnergyCapacity();
    }

    private void updateEnergyCapacity() {
        this.maxStorage = baseStorage + connectedNodes.size() * storagePerNode;
        this.maxInput = baseInput + connectedNodes.size() * ioPerNode;
        this.maxOutput = baseOutput + connectedNodes.size() * ioPerNode;
    }

    public String getNetworkId() {
        return this.networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

}
