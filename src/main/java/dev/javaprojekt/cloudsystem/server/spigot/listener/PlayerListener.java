package dev.javaprojekt.cloudsystem.server.spigot.listener;

import dev.javaprojekt.cloudsystem.cloud.config.cinfo.ServerInfoFileManager;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerInfo;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerListener implements Listener {

   /* @EventHandler
    public void on(AsyncPlayerPreLoginEvent event) {
        if (!event.getAddress().getHostAddress().equals(ServerInfoFileManager.getCloudConfig().getCommanderAddress())) {
            System.out.println("Host: " + event.getAddress().getHostAddress());
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, "§cYou are not allowed to directly join servers.");
        }
    }

    */

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CloudServerInfo serverInfo = CloudAPI.getInstance().getThisServer().getCloudServerInfo();
        ArrayList<UUID> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(all -> players.add(all.getUniqueId()));
        serverInfo.setOnlinePlayers(players);
        CloudAPI.getInstance().updateCloudServerInfo( serverInfo);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(CloudSpigot.getInstance(), () -> {
            CloudServerInfo serverInfo = CloudAPI.getInstance().getThisServer().getCloudServerInfo();
            ArrayList<UUID> players = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(all -> players.add(all.getUniqueId()));
            serverInfo.setOnlinePlayers(players);
            CloudAPI.getInstance().updateCloudServerInfo(serverInfo);
        },5);
    }

}
