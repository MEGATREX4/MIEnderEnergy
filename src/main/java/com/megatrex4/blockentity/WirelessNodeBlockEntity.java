package com.megatrex4.blockentity;

import com.megatrex4.EnderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessNodeBlockEntity extends BlockEntity {
    private WirelessNetwork network;

    public WirelessNodeBlockEntity(BlockPos pos, BlockState state) {
        super(EnderBlockEntity.WIRELESS_NODE, pos, state);
    }

    public void findAndJoinNetwork() {
        if (this.network == null) {
            this.network = new WirelessNetwork();
            this.network.addNode(this);
        }
    }

    public WirelessNetwork getNetwork() {
        return network;
    }

    public void setNetwork(WirelessNetwork network) {
        this.network = network;
    }

    public void resetNetwork() {
        this.network = null;
    }
}
