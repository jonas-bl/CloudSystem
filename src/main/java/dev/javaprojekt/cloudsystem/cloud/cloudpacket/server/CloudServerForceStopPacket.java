package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudServerForceStopPacket implements ICloudPacket {

    private UUID serverUUID;

    public CloudServerForceStopPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
