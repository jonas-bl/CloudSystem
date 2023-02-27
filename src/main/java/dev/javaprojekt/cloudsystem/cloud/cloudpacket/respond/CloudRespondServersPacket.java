package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class CloudRespondServersPacket implements ICloudPacket {

    private UUID requestUUID;
    private ArrayList<CloudServerObject> servers;

    public CloudRespondServersPacket(UUID requestUUID, ArrayList<CloudServerObject> servers) {
        this.requestUUID = requestUUID;
        this.servers = servers;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public ArrayList<CloudServerObject> getServers() {
        return servers;
    }
}
