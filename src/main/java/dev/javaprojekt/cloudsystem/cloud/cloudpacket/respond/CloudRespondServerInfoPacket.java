package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;

import java.util.ArrayList;
import java.util.UUID;

public class CloudRespondServerInfoPacket implements ICloudPacket {

    private UUID requestUUID;
    private CloudServerObject cloudServer;

    public CloudRespondServerInfoPacket(UUID requestUUID, CloudServerObject cloudServer) {
        this.requestUUID = requestUUID;
        this.cloudServer = cloudServer;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public CloudServerObject getCloudServer() {
        return cloudServer;
    }
}
