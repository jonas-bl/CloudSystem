package dev.javaprojekt.cloudsystem.cloud.cloudpacket.request;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudRequestServerStartPacket implements ICloudPacket {

    private String targetSlave;
    private String serverTemplate;
    private String copyTemplate;
    private UUID serverUUID;

    public CloudRequestServerStartPacket(String targetSlave, String serverTemplate, String copyTemplate, UUID serverUUID) {
        this.targetSlave = targetSlave;
        this.copyTemplate = copyTemplate;
        this.serverTemplate = serverTemplate;
        this.serverUUID = serverUUID;
    }

    public String getServerTemplate() {
        return serverTemplate;
    }

    public String getCopyTemplate() {
        return copyTemplate;
    }

    public String getTargetSlave() {
        return targetSlave;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
