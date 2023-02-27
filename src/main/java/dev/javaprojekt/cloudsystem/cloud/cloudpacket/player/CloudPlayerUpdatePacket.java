package dev.javaprojekt.cloudsystem.cloud.cloudpacket.player;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;

import java.util.UUID;

public class CloudPlayerUpdatePacket implements ICloudPacket {

    private UUID uuid;
    private CloudPlayer cloudPlayer;
    private boolean remove;

    public CloudPlayerUpdatePacket(UUID uuid, CloudPlayer cloudPlayer, boolean remove) {
        this.uuid = uuid;
        this.cloudPlayer = cloudPlayer;
        this.remove = remove;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isRemove() {
        return remove;
    }
}
