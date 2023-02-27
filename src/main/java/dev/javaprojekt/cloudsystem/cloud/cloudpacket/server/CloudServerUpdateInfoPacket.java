package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerInfo;

import java.util.UUID;

public class CloudServerUpdateInfoPacket implements ICloudPacket {

    UUID serverUUID;
    CloudServerInfo cloudServerInfo;

    public CloudServerUpdateInfoPacket(UUID serverUUID, CloudServerInfo cloudServerInfo) {
        this.serverUUID = serverUUID;
        this.cloudServerInfo = cloudServerInfo;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public CloudServerInfo getCloudServerInfo() {
        return cloudServerInfo;
    }
}
