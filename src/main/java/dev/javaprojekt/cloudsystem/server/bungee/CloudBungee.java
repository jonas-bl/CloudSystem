package dev.javaprojekt.cloudsystem.server.bungee;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.config.cinfo.ServerInfoFileManager;
import dev.javaprojekt.cloudsystem.event.CloudEventManager;
import dev.javaprojekt.cloudsystem.file.FileManager;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.bungee.command.CloudCommand;
import dev.javaprojekt.cloudsystem.server.bungee.command.HubCommand;
import dev.javaprojekt.cloudsystem.server.bungee.fakeplayers.FakePlayerManager;
import dev.javaprojekt.cloudsystem.server.bungee.listener.CloudListener;
import dev.javaprojekt.cloudsystem.server.bungee.listener.PermissionListener;
import dev.javaprojekt.cloudsystem.server.bungee.listener.PlayerListener;
import dev.javaprojekt.cloudsystem.socket.SocketClient;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class CloudBungee extends Plugin {

    /*

    Created on 28th February 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing may result in criminal prosecution.

     */

    private static CloudBungee instance;

    public static CloudBungee getInstance() {
        return instance;
    }

    private static UUID serverUUID;

    private static SocketClient socketClient;

    public static UUID getServerUUID() {
        return serverUUID;
    }

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    public static String getServerName() {
        return ServerInfoFileManager.getCloudConfig().getServerName();
    }

    public void onEnable() {
        instance = this;
        new CloudAPI();
        ServerInfoFileManager.init();
        SocketClientType.setSocketClientType(SocketClientType.PROXY);
        socketClient = new SocketClient(ServerInfoFileManager.getCloudConfig().getCommanderAddress(), 24300);
        serverUUID = FileManager.getCurrentServerUUID();
        socketClient.setServerUUID(getServerUUID());
        Thread thread = new Thread(socketClient);
        thread.start();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerListener());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PermissionListener());
        CloudAPI.getInstance().getEventManager().registerEvents(new CloudListener());
    }


    public void onDisable() {
        getSocketClient().getChannel().writeAndFlush(new CloudServerStopPacket(getServerUUID()));
        getSocketClient().stop();
    }
}
