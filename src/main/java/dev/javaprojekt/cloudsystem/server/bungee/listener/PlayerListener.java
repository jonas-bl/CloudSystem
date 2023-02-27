package dev.javaprojekt.cloudsystem.server.bungee.listener;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.player.CloudPlayerUpdatePacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.cloud.server.ProxyServerManager;
import dev.javaprojekt.cloudsystem.event.CloudEventHandler;
import dev.javaprojekt.cloudsystem.event.CloudEventManager;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudChangeServerEvent;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudConnectEvent;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudDisconnectEvent;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.bungee.fakeplayers.FakePlayerManager;
import dev.javaprojekt.cloudsystem.server.bungee.lobbychoose.LobbyChooser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerListPingEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    private static ArrayList<ProxiedPlayer> noKick = new ArrayList<>();

    public static ArrayList<ProxiedPlayer> getNoKick() {
        return noKick;
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {
     /*   ServerPing.Players players = event.getResponse().getPlayers();
        players.setOnline(FakePlayerManager.getOnline() + ProxyServer.getInstance().getOnlineCount());
       players.setMax(120);
        ServerPing ping = event.getResponse();
        ServerPing.Protocol protocol = ping.getVersion();
        protocol.setName("§bMyCloud");
        ping.setVersion(protocol);
       // event.getResponse().setDescription();

      */
    }

    @EventHandler
    public void on(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CloudPlayer cloudPlayer = new CloudPlayer(player.getUniqueId(), player.getName(), null, CloudAPI.getInstance().getThiServerName());
        CloudAPI.getInstance().getEventManager().callEvent(new PlayerCloudConnectEvent(cloudPlayer), true);
        CloudAPI.getInstance().channelWriteAndFlush(new CloudPlayerUpdatePacket(player.getUniqueId(), cloudPlayer, false));
    }

    @EventHandler
    public void on(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CloudPlayer cloudPlayer = new CloudPlayer(player.getUniqueId(), player.getName(), null, CloudAPI.getInstance().getThiServerName());
        CloudAPI.getInstance().getEventManager().callEvent(new PlayerCloudDisconnectEvent(cloudPlayer), true);
        CloudAPI.getInstance().channelWriteAndFlush(new CloudPlayerUpdatePacket(player.getUniqueId(), cloudPlayer, true));
    }

    @EventHandler
    public void on(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CloudPlayer cloudPlayer = new CloudPlayer(player.getUniqueId(), player.getName(), player.getServer().getInfo().getName(), CloudAPI.getInstance().getThiServerName());
        CloudServerObject from = event.getFrom() == null ? null : CloudAPI.getInstance().getServer(event.getFrom().getName());
        CloudAPI.getInstance().getEventManager().callEvent(new PlayerCloudChangeServerEvent(cloudPlayer, from), true);
        CloudAPI.getInstance().channelWriteAndFlush(new CloudPlayerUpdatePacket(player.getUniqueId(), cloudPlayer, false));
    }


    @EventHandler
    public void on(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.getServer() != null) {
            return;
        }
        ServerInfo serverInfo = new LobbyChooser().getFreeLobby();
        if (serverInfo == null) {
            player.disconnect("§cCurrently is no Lobby server available");
            return;
        }
        event.setTarget(serverInfo);
    }

    @EventHandler
    public void on(ServerKickEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(player.getServer() == null)return;

        if(getNoKick().contains(player))return;
        if(event.getPlayer().hasPermission("cloudsystem.admin")) {
           player.sendMessage ("You called ServerKickEvent in CloudSystem");
        }
        ServerInfo serverInfo = new LobbyChooser().getFreeLobby();
        event.setCancelled(true);
        if(!player.getServer().getInfo().getName().equals(event.getKickedFrom().getName())) {
            event.setCancelServer(null);
            return;
        }
        String reason = TextComponent.toLegacyText(event.getKickReasonComponent());
        player.sendMessage(reason);
        if (serverInfo == null) {
            if (!player.hasPermission("cloudsystem.shutdown.bypass")) {
                player.disconnect("§cCurrently is no Lobby server available");
            }
            return;
        }
        event.setCancelServer(serverInfo);
    }
}
