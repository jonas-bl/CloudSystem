package dev.javaprojekt.cloudsystem.cloud.cloudpacket.request;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.enums.CloudRequestType;

import java.util.UUID;

public class CloudRequestPacket implements ICloudPacket {

    private UUID requestUUID;
    private CloudRequestType requestType;
    private String key;

    public CloudRequestPacket(UUID requestUUID, CloudRequestType requestType, String key) {
        this.requestUUID = requestUUID;
        this.requestType = requestType;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public CloudRequestType getRequestType() {
        return requestType;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }
}
