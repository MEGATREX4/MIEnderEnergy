package com.megatrex4.block.entity;

import java.util.UUID;

public interface UUIDConverter {

    /**
     * Converts a UUID to a string representation.
     *
     * @param uuid the UUID to convert
     * @return the string representation of the UUID
     */
    String uuidToString(UUID uuid);

    /**
     * Converts a string representation back to a UUID.
     *
     * @param uuidString the string to convert to a UUID
     * @return the UUID object
     */
    UUID stringToUUID(String uuidString);
}

