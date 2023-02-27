package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudServerCommandPacket implements ICloudPacket {

    private UUID serverUUID;
    private String command;

    public CloudServerCommandPacket(UUID serverUUID, String command) {
        this.serverUUID = serverUUID;
        this.command = command;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public String getCommand() {
        return command;
    }
}
