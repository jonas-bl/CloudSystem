package dev.javaprojekt.cloudsystem.cloud.player;

import java.util.HashMap;
import java.util.UUID;

public class CloudPlayerManager {

    private static CloudPlayerManager instance;

    public static CloudPlayerManager getInstance() {
        return instance;
    }

    private static HashMap<UUID, CloudPlayer> cloudPlayers;

    public static HashMap<UUID, CloudPlayer> getCloudPlayers() {
        return cloudPlayers;
    }

    public CloudPlayerManager() {
        instance = this;
        cloudPlayers = new HashMap<>();
    }

    public CloudPlayer getPlayer(UUID uuid) {
        return getCloudPlayers().get(uuid);
    }

    public void updatePlayer(CloudPlayer cloudPlayer) {
        cloudPlayers.put(cloudPlayer.getUUID(), cloudPlayer);
    }

    public void removePlayer(UUID uuid) {
        cloudPlayers.remove(uuid);
    }
}
