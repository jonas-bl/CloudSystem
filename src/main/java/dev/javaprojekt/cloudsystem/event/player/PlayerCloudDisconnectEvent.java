package dev.javaprojekt.cloudsystem.event.player;

import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.event.CloudEvent;

public class PlayerCloudDisconnectEvent implements CloudEvent {

    private CloudPlayer cloudPlayer;

    public PlayerCloudDisconnectEvent(CloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }
}

