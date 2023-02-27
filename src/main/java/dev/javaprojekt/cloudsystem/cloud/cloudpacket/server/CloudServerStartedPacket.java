package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.io.Serializable;
import java.util.UUID;

public class CloudServerStartedPacket implements ICloudPacket {

    UUID serverUUID;

    public CloudServerStartedPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
