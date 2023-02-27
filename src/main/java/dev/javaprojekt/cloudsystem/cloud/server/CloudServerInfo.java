package dev.javaprojekt.cloudsystem.cloud.server;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudServerInfo implements Serializable {

    private static final long serialVersionUID = 145672346573L;

    private UUID serverUUID;
    private String serverName;
    private String motd;
    private String extra;
    private ArrayList<UUID> onlinePlayers;
    private String serverState;
    private Integer maxPlayers;

    public CloudServerInfo(UUID serverUUID, String serverName, String motd, String extra, ArrayList<UUID> onlinePlayers, Integer maxPlayers, String serverState) {
        this.serverUUID = serverUUID;
        this.serverName = serverName;
        this.motd = motd;
        this.extra = extra;
        this.onlinePlayers = onlinePlayers;
        this.serverState = serverState;
        this.maxPlayers = maxPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public void setServerUUID(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ArrayList<UUID> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(ArrayList<UUID> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public String getServerState() {
        return serverState;
    }

    public void setServerState(String serverState) {
        this.serverState = serverState;
    }
}
