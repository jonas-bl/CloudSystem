package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;

import java.util.UUID;

public class CloudServerConstructedPacket implements ICloudPacket {

    private UUID serverUUID;
    private CloudServer cloudServer;

    public CloudServerConstructedPacket(UUID serverUUID, CloudServer cloudServer) {
        this.serverUUID = serverUUID;
        this.cloudServer = cloudServer;
    }

    public CloudServer getCloudServer() {
        return cloudServer;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
