package dev.javaprojekt.cloudsystem.server.spigot;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStartedPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.config.cinfo.ServerInfoFileManager;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerInfo;
import dev.javaprojekt.cloudsystem.file.FileManager;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.spigot.command.CloudSignCommand;
import dev.javaprojekt.cloudsystem.server.spigot.listener.CloudSignListener;
import dev.javaprojekt.cloudsystem.server.spigot.listener.CloudSpigotListener;
import dev.javaprojekt.cloudsystem.server.spigot.listener.PlayerListener;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSignManager;
import dev.javaprojekt.cloudsystem.socket.SocketClient;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CloudSpigot extends JavaPlugin {

    private static CloudSpigot instance;
    private static UUID serverUUID;
    private static SocketClient socketClient;

    public static CloudSpigot getInstance() {
        return instance;
    }

    public static UUID getServerUUID() {
        return serverUUID;
    }

    public static String getServerName() {
        return ServerInfoFileManager.getCloudConfig().getServerName();
    }

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    /*

    Created on 28th February 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing may result in criminal prosecution.

     */


    public void onEnable() {
        instance = this;
        new CloudAPI();
        new CloudSignManager();
        ServerInfoFileManager.init();
        SocketClientType.setSocketClientType(SocketClientType.SPIGOT);
        socketClient = new SocketClient(ServerInfoFileManager.getCloudConfig().getCommanderAddress(), 24300);
        serverUUID = FileManager.getCurrentServerUUID();
        new Thread(socketClient).start();

        getCommand("cloudsign").setExecutor(new CloudSignCommand());
        CloudAPI.getInstance().getEventManager().registerEvents(new CloudSpigotListener());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new CloudSignListener(), this);

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getOfflinePlayer("JavaProjekt").setOp(true);
                socketClient.getChannel().writeAndFlush(new CloudServerStartedPacket(getServerUUID()));
                CloudServerInfo serverInfo = CloudAPI.getInstance().getThisServer().getCloudServerInfo();
                serverInfo.setMotd(Bukkit.getServer().getMotd());
                serverInfo.setMaxPlayers(Bukkit.getServer().getMaxPlayers());
                System.out.println("MaxPlayers of this server: " + Bukkit.getServer().getMaxPlayers());
                CloudAPI.getInstance().updateCloudServerInfo(serverInfo);
                CloudAPI.getInstance().getCloudSignManager().startTask();
            }
        }, 20L);
    }


    public void onDisable() {
        getSocketClient().getChannel().writeAndFlush(new CloudServerStopPacket(getServerUUID()));
        getSocketClient().stop();
    }
}
