package dev.javaprojekt.cloudsystem.cloud.cloudpacket.player;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;

public class CloudPlayerSendServerPacket implements ICloudPacket {

    private CloudPlayer cloudPlayer;
    private String server;

    public CloudPlayerSendServerPacket(CloudPlayer cloudPlayer, String server) {
        this.cloudPlayer = cloudPlayer;
        this.server = server;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }

    public String getServer() {
        return server;
    }
}
