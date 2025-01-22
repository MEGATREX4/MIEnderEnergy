package com.megatrex4.block.entity;

import com.megatrex4.MIEnderEnergy;
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
        if (this.uniqueId == null) {
            this.uniqueId = UUID.randomUUID();
            MIEnderEnergy.LOGGER.info("UUID generated: " + uniqueId);
        } else {
            MIEnderEnergy.LOGGER.info("UUID already set: " + uniqueId);
        }
    }


    // Getter for the UUID
    public UUID getUniqueId() {
        MIEnderEnergy.LOGGER.info("get UUID: " + uniqueId);
        return uniqueId;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (uniqueId != null) {
            String uuidString = uniqueId.toString(); // Convert UUID to String
            nbt.putString("uuid", uuidString); // Store it as a String
            MIEnderEnergy.LOGGER.info("write UUID: " + uuidString);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("uuid")) {
            String uuidString = nbt.getString("uuid");
            this.uniqueId = UUID.fromString(uuidString); // Convert String back to UUID
            MIEnderEnergy.LOGGER.info("read UUID: " + uuidString);
        }
    }
}
