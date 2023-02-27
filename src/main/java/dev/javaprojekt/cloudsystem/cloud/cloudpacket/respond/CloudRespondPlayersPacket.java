package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class CloudRespondPlayersPacket implements ICloudPacket {

    private UUID requestUUID;
    private ArrayList<CloudPlayer> cloudPlayers;

    public CloudRespondPlayersPacket(UUID requestUUID, ArrayList<CloudPlayer> cloudPlayers) {
        this.requestUUID = requestUUID;
        this.cloudPlayers = cloudPlayers;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public ArrayList<CloudPlayer> getCloudPlayers() {
        return cloudPlayers;
    }
}
