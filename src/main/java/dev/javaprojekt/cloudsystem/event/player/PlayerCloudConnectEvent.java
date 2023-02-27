package dev.javaprojekt.cloudsystem.event.player;

import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.event.CloudEvent;
import dev.javaprojekt.cloudsystem.event.CloudEventHandler;

public class PlayerCloudConnectEvent implements CloudEvent {

    private CloudPlayer cloudPlayer;

    public PlayerCloudConnectEvent(CloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
    }

    public CloudPlayer getCloudPlayer() {
        return cloudPlayer;
    }
}
