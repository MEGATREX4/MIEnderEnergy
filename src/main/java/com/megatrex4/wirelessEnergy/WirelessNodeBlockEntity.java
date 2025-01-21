package com.megatrex4.wirelessEnergy;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.MachineBaseBlockEntity;

public class WirelessNodeBlockEntity extends MachineBaseBlockEntity {
    private String networkId = "";

    public WirelessNodeBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlockEntities.WIRELESS_NODE, pos, state);
    }

    public String getNetworkId() {
        return this.networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
}
