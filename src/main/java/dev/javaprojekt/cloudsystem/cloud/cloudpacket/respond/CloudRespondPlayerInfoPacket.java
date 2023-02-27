package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;

import java.util.UUID;

public class CloudRespondPlayerInfoPacket implements ICloudPacket {

    private UUID requestUUID;
    private CloudPlayer cloudPlayer;

    public CloudRespondPlayerInfoPacket(UUID requestUUID, CloudPlayer cloudPlayer) {
        this.requestUUID = requestUUID;
        this.cloudPlayer = cloudPlayer;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }
}
