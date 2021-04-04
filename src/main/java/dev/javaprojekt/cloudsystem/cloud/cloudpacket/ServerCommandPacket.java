package dev.javaprojekt.cloudsystem.cloud.cloudpacket;

import java.util.UUID;

public class ServerCommandPacket {

    private UUID serverUUID;
    private String command;

    public ServerCommandPacket(UUID serverUUID, String command) {
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
