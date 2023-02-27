package dev.javaprojekt.cloudsystem.server.bungee.lobbychoose;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.Comparator;

public class LobbyChooser {

    public ServerInfo getFreeLobby() {
        ArrayList<ServerInfo> lobbies = new ArrayList<>();
        for (ServerInfo serverInfo : ProxyServer.getInstance().getServers().values()) {
            if (serverInfo.getName().startsWith("Lobby-")) {
                lobbies.add(serverInfo);
            }
        }
        if (lobbies.isEmpty()) {
            return null;
        }
        Comparator<ServerInfo> compareById = Comparator.comparing(serverInfo -> serverInfo.getPlayers().size());
        lobbies.sort(compareById);
        return lobbies.get(0);
    }
}
