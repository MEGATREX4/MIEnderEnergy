package com.megatrex4.blockentity;

import java.util.ArrayList;

public class WirelessNetwork {
    private final ArrayList<WirelessNodeBlockEntity> nodes = new ArrayList<>();
    private WirelessControllerBlockEntity master;

    public void addNode(WirelessNodeBlockEntity node) {
        if (!nodes.contains(node) && nodes.size() < 5000) {
            nodes.add(node);
        }
    }

    public void removeNode(WirelessNodeBlockEntity node) {
        nodes.remove(node);
        rebuild();
    }

    private void rebuild() {
        this.master = null;
        for (WirelessNodeBlockEntity node : nodes) {
            node.findAndJoinNetwork();
        }
    }

    public void merge(WirelessNetwork network) {
        if (network != this) {
            ArrayList<WirelessNodeBlockEntity> newNodes = new ArrayList<>(network.nodes);
            network.clear(false);
            for (WirelessNodeBlockEntity node : newNodes) {
                node.setNetwork(this);
            }
            if (network.master != null && this.master == null) {
                this.master = network.master;
            }
        }
    }

    private void clear(boolean clearBlockEntities) {
        if (clearBlockEntities) {
            for (WirelessNodeBlockEntity node : nodes) {
                node.resetNetwork();
            }
        }
        nodes.clear();
    }

    public ArrayList<WirelessNodeBlockEntity> getNodes() {
        return nodes;
    }

    public void setMaster(WirelessControllerBlockEntity master) {
        this.master = master;
    }
}
