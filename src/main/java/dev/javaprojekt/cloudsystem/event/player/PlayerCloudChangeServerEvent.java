package dev.javaprojekt.cloudsystem.event.player;

import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.event.CloudEvent;

public class PlayerCloudChangeServerEvent implements CloudEvent {

    private CloudPlayer cloudPlayer;
    private CloudServerObject from;

    public PlayerCloudChangeServerEvent(CloudPlayer cloudPlayer, CloudServerObject from) {
        this.cloudPlayer = cloudPlayer;
        this.from = from;
    }

    public CloudServerObject getFrom() {
        return from;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }
}
