package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudServerRemovePacket implements ICloudPacket {

    private String serverName;
    private UUID serverUUID;

    public CloudServerRemovePacket(String serverName, UUID serverUUID) {
        this.serverUUID = serverUUID;
        this.serverName = serverName;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public String getServerName() {
        return serverName;
    }
}
