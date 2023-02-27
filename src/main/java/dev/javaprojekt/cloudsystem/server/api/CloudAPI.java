package dev.javaprojekt.cloudsystem.server.api;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CustomCloudMessagePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.player.CloudPlayerSendServerPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStartPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.enums.CloudRequestType;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerCommandPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerUpdateInfoPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerInfo;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;
import dev.javaprojekt.cloudsystem.cloud.util.future.AsyncFutureRequest;
import dev.javaprojekt.cloudsystem.event.CloudEventManager;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessage;
import dev.javaprojekt.cloudsystem.event.server.CloudServerUpdateEvent;
import dev.javaprojekt.cloudsystem.event.server.CloudServerUpdateType;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSign;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSignManager;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CloudAPI {

    private static CloudAPI instance;

    public CloudAPI() {
        instance = this;
        new CloudEventManager();
    }

    public static CloudAPI getInstance() {
        return instance;
    }

    public void sendCustomCloudMessage(CustomCloudMessage message, String server) {
        if (server.equals(getThisServer().getName())) return;
        channelWriteAndFlush(new CustomCloudMessagePacket(message, server));
    }

    public CloudServerObject getThisServer() {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            return getServerByUUID(CloudBungee.getServerUUID());
        }
        return getServerByUUID(CloudSpigot.getServerUUID());
    }

    public CloudEventManager getEventManager() {
        return CloudEventManager.getInstance();
    }

    public String getThiServerName() {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            return CloudBungee.getServerName();
        }
        return CloudSpigot.getServerName();
    }

    private UUID getThiServerUUID() {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            return CloudBungee.getServerUUID();
        }
        return CloudSpigot.getServerUUID();
    }


    public void channelWriteAndFlush(Object object) {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(object);
        } else {
            CloudSpigot.getSocketClient().getChannel().writeAndFlush(object);
        }
    }

    public ArrayList<CloudPlayer> getGlobalPlayers() {
        try {
            return (ArrayList<CloudPlayer>) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.PLAYERS, null)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public CloudPlayer getPlayer(UUID uuid) {
        try {
            return (CloudPlayer) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.PLAYER_INFO, uuid.toString())).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ServerTemplate> getTemplates() {
        try {
            return (ArrayList<ServerTemplate>) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.TEMPLATES, null)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public HashMap<String, ServerTemplate> getTemplatesMap() {
        HashMap<String, ServerTemplate> map = new HashMap<>();
        getTemplates().forEach(all -> {
           map.put(all.getName(), all);
        });
        return map;
    }

    public ArrayList<CloudServerObject> getCloudServers() {
        try {
            return (ArrayList<CloudServerObject>) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.SERVERS, null)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<CloudServerObject> getCloudServersByTemplate(String template) {
        ArrayList<CloudServerObject> list = new ArrayList<>();
        getCloudServers().forEach(all -> {
            if (all.getTemplate().getName().equals(template)) list.add(all);
        });
        return list;
    }

    public ArrayList<String> getCloudServersByTemplateString(String template) {
        ArrayList<String> list = new ArrayList<>();
        getCloudServers().forEach(all -> {
            if (all.getTemplate().getName().equals(template)) list.add(all.getName());
        });
        return list;
    }

    public ArrayList<String> getCloudServersByTemplateString(String template, String state) {
        ArrayList<String> list = new ArrayList<>();
        getCloudServers().forEach(all -> {
            if (all.getTemplate().getName().equals(template) && all.getCloudServerInfo().getServerState().equalsIgnoreCase(state)) list.add(all.getName());
        });
        return list;
    }

    public CloudServerObject getServer(String name) {
        try {
            return (CloudServerObject) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.SERVER_INFO_NAME, name)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CloudServerObject getServerByUUID(UUID name) {
        try {
            return (CloudServerObject) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.SERVER_INFO_UUID, name.toString())).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ServerTemplate getTemplate(String name) {
        try {
            return (ServerTemplate) new AsyncFutureRequest().fetch(new CloudRequestPacket(UUID.randomUUID(), CloudRequestType.TEMPLATE_INFO, name)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startServer(ServerTemplate template) {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), null, UUID.randomUUID()));
        } else {
            CloudSpigot.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), null, UUID.randomUUID()));
        }
    }

    public void startServer(ServerTemplate template, UUID uuid, String copyTemplate) {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), null, UUID.randomUUID()));
        } else {
            CloudSpigot.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), copyTemplate, uuid));
        }
    }

    public void sendCommand(String server, String command) {
        CloudServerObject cloudServer = getServer(server);
        if (cloudServer == null) return;
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudServerCommandPacket(cloudServer.getServerUUID(), command));
        } else {
            CloudSpigot.getSocketClient().getChannel().writeAndFlush(new CloudServerCommandPacket(cloudServer.getServerUUID(), command));
        }
    }

    public void updateCloudServerInfo(CloudServerInfo cloudServerInfo) {
        channelWriteAndFlush(new CloudServerUpdateInfoPacket(getThiServerUUID(), cloudServerInfo));
        getEventManager().callEvent(new CloudServerUpdateEvent(getThiServerName(), CloudServerUpdateType.INFO), true);
    }

    public void setInGame() {
       CloudServerInfo serverInfo =  getThisServer().getCloudServerInfo();
       if(serverInfo.getServerState().equalsIgnoreCase("INGAME")) {
           return;
       }
       serverInfo.setServerState("INGAME");
       updateCloudServerInfo(serverInfo);
       ServerTemplate template = getThisServer().getTemplate();
       if(getCloudServersByTemplate(template.getName()).size() >= template.getMaxOnline())return;
       startServer(template);
    }

    public void sendPlayer(CloudPlayer cloudPlayer, String targetServer) {
        if(cloudPlayer == null)return;
        channelWriteAndFlush(new CloudPlayerSendServerPacket(cloudPlayer, targetServer));
    }

    public void stopServer(CloudServerObject server) {
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStopPacket(server.getServerUUID()));
        } else {
            CloudSpigot.getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStopPacket(server.getServerUUID()));
        }
    }

    public HashMap<String, ArrayList<CloudServerObject>> getCloudServersMap() {
        HashMap<String, ArrayList<CloudServerObject>> map = new HashMap<>();
        getTemplates().forEach(all -> {
            map.put(all.getName(), getCloudServersByTemplate(all.getName()));
        });
        return map;
    }

    public CloudSignManager getCloudSignManager() {
        return CloudSignManager.getInstance();
    }
}
