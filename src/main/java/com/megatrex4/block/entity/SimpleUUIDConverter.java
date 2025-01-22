package com.megatrex4.block.entity;

import java.util.UUID;

public class SimpleUUIDConverter implements UUIDConverter {

    @Override
    public String uuidToString(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID stringToUUID(String uuidString) {
        return UUID.fromString(uuidString);
    }
}
