package dev.javaprojekt.cloudsystem.cloud.config.cinfo;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;

import java.util.UUID;

public class ServerInfoFile {

    private String commanderAddress;
    private UUID serverUUID;
    private String serverName;

    public ServerInfoFile(String commanderAddress, UUID serverUUID, String serverName) {
        this.commanderAddress = commanderAddress;
        this.serverUUID = serverUUID;
        this.serverName = serverName;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public String getServerName() {
        return serverName;
    }

    public String getCommanderAddress() {
        return commanderAddress;
    }
}
