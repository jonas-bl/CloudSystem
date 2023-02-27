package dev.javaprojekt.cloudsystem.cloud.cloudpacket.request;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudRequestServerStopPacket implements ICloudPacket {

    UUID serverUUID;

    public CloudRequestServerStopPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
