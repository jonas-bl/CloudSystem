package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.io.Serializable;
import java.util.UUID;

public class CloudServerStopPacket implements ICloudPacket {


    UUID serverUUID;

    public CloudServerStopPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
