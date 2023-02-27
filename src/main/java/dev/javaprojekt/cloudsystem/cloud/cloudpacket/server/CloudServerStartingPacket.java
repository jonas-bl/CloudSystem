package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudServerStartingPacket implements ICloudPacket {

    UUID serverUUID;

    public CloudServerStartingPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
