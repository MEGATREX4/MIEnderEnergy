package com.megatrex4.block.entity;

import com.megatrex4.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class WirelessControllerBlockEntity extends BlockEntity {

    private UUID uniqueId; // Unique identifier for the block entity

    public WirelessControllerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRELESS_CONTROLLER_BLOCK_ENTITY, pos, state);
        this.uniqueId = UUID.randomUUID(); // Generate a new UUID when created
    }

    // Getter for the UUID
    public UUID getUniqueId() {
        return uniqueId;
    }

    // Write data to NBT for persistence
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (uniqueId != null) {
            nbt.putUuid("UniqueId", uniqueId);
        }
    }

    // Read data from NBT
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("UniqueId")) {
            this.uniqueId = nbt.getUuid("UniqueId");
        }
    }
}
