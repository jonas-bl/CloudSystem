package dev.javaprojekt.cloudsystem.cloud.cloudpacket.message;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessage;

public class CustomCloudMessagePacket implements ICloudPacket {

    private CustomCloudMessage cloudMessage;
    private String targetServer;

    public CustomCloudMessagePacket(CustomCloudMessage cloudMessage, String targetServer) {
        this.cloudMessage = cloudMessage;
        this.targetServer = targetServer;
    }

    public CustomCloudMessage getCloudMessage() {
        return cloudMessage;
    }

    public String getTargetServer() {
        return targetServer;
    }
}
