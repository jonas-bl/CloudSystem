package dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.event.CloudSendEventPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CloudNotifyPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CustomCloudMessagePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.enums.CloudNotifyType;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.player.CloudPlayerSendServerPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.player.CloudPlayerUpdatePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStartPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServersAddedPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.enums.CloudRequestType;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond.*;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.*;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave.CloudSlaveConnectPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave.CloudSlaveDisconnectPacket;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayerManager;
import dev.javaprojekt.cloudsystem.cloud.server.*;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.UUID;

public class CommanderPacketHandler {

    public static void handlePacket(Channel channel, Object income) {
        if (income.getClass() == CloudRequestPacket.class) {
            handleRequestPacket(channel, income);
            return;
        }
        if (income.getClass() == CloudServerStartedPacket.class) {
            CloudServerStartedPacket packet = (CloudServerStartedPacket) income;
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID());
            if(cloudServer == null) {
                channel.writeAndFlush(new CloudRequestServerStopPacket(packet.getServerUUID()));
                return;
            }
            cloudServer.setNettyChannel(channel);
            cloudServer.setServerState(ServerState.ONLINE);
            CloudLogger.getInstance().log("The server " + cloudServer.getName() + " is now online.");
            ServerTemplate template = cloudServer.getTemplate();
            ServerStarterQueue.getInstance().removeStarting(packet.getServerUUID());
            assert template.getServerVersion().equals(ServerVersion.SPIGOT);
            ArrayList<CloudServer> proxies = CloudServerManager.getInstance().getServersByTemplate("Proxy");
            for (CloudServer proxy : proxies) {
                if(proxy.getNettyChannel() == null)continue;
                proxy.getNettyChannel().writeAndFlush(new CloudServerAddPacket(cloudServer.getName(), cloudServer.getAdress(), cloudServer.getPort()));
                proxy.getNettyChannel().writeAndFlush(new CloudNotifyPacket(CloudNotifyType.SERVER_STARTED, cloudServer.getName()));
            }
            return;
        }
        if (income.getClass() == CloudServerConstructedPacket.class) {
            CloudServerConstructedPacket packet = (CloudServerConstructedPacket) income;
            CloudServerManager.getInstance().getCloudServer().put(packet.getServerUUID(), packet.getCloudServer());
            return;
        }
        if (income.getClass() == CloudRequestServersAddedPacket.class) {
            ArrayList<CloudServer> proxies = CloudServerManager.getInstance().getServersByTemplate("Proxy");
            for (CloudServer cloudServer : CloudServerManager.getInstance().getCloudServer().values()) {
                if (cloudServer.getServerState() != ServerState.ONLINE) continue;
                if (cloudServer.getTemplate().getServerVersion() == ServerVersion.SPIGOT) {
                    for (CloudServer proxy : proxies) {
                        proxy.getNettyChannel().writeAndFlush(new CloudServerAddPacket(cloudServer.getName(), cloudServer.getAdress(), cloudServer.getPort()));
                    }
                }
            }
            return;
        }
        if (income.getClass() == CloudServerStopPacket.class) {
            CloudServerStopPacket packet = (CloudServerStopPacket) income;
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID());
            if (cloudServer == null) return;
            CloudServerManager.getInstance().getCloudServer().remove(packet.getServerUUID());
            CloudLogger.getInstance().log("The server " + cloudServer.getName() + " is now offline.");
            CloudSlaveManager.getInstance().getSlaveByName(cloudServer.getTemplate().getSlave()).getChannel().writeAndFlush(packet);
            for (CloudServer proxy : CloudServerManager.getInstance().getServersByTemplate("Proxy")) {
                proxy.getNettyChannel().writeAndFlush(new CloudServerRemovePacket(cloudServer.getName(), cloudServer.getServerUUID()));
                proxy.getNettyChannel().writeAndFlush(new CloudNotifyPacket(CloudNotifyType.SERVER_STOPPED, cloudServer.getName()));
            }
            return;
        }
        if (income.getClass() == CloudSlaveConnectPacket.class) {
            CloudSlaveConnectPacket packet = (CloudSlaveConnectPacket) income;
            CloudSlaveObject cloudSlaveObject = CloudSlaveManager.getInstance().getSlaveByName(packet.getSlaveName());
            if (cloudSlaveObject == null) {
                CloudLogger.getInstance().log("ERROR: Received a CloudSlaveConnectPacket but the slave does not exist!");
                return;
            }
            cloudSlaveObject.setServerState(ServerState.ONLINE);
            cloudSlaveObject.setChannel(channel);
            CloudLogger.getInstance().log("The CloudSlave " + packet.getSlaveName() + " connected to the Commander.");
            return;
        }
        if (income.getClass() == CloudSlaveDisconnectPacket.class) {
            CloudSlaveDisconnectPacket packet = (CloudSlaveDisconnectPacket) income;
            CloudSlaveObject cloudSlaveObject = CloudSlaveManager.getInstance().getSlaveByName(packet.getSlaveName());
            if (cloudSlaveObject == null) {
                CloudLogger.getInstance().log("ERROR: Received a CloudSlaveDisconnectPacket but the slave does not exist!");
                return;
            }
            cloudSlaveObject.setServerState(ServerState.OFFLINE);
            cloudSlaveObject.setChannel(null);
            CloudLogger.getInstance().log("The CloudSlave " + packet.getSlaveName() + " disconnected from the Commander.");
            return;
        }
        if (income.getClass() == CloudServerCommandPacket.class) {
            CloudServerCommandPacket packet = (CloudServerCommandPacket) income;
            CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).writeToChannel(packet);
            return;
        }
        if (income.getClass() == CloudRequestServerStopPacket.class) {
            CloudRequestServerStopPacket packet = (CloudRequestServerStopPacket) income;
            if(CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).getNettyChannel() == null) {
                CloudLogger.getInstance().log("Force stopped server " + CloudServerManager.getInstance().getCloudServer().get(packet.getServerUUID()).getName());
                ServerTemplate template = CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).getTemplate();
                String slave = template.getSlave();
                CloudSlaveManager.getInstance().getSlaveByName(slave).getChannel().writeAndFlush(packet);
                CloudServerManager.getInstance().getCloudServer().remove(packet.getServerUUID());
                ServerStarterQueue.getInstance().getStarting().remove(packet.getServerUUID());
                return;
            }
            CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).writeToChannel(packet);
            return;
        }
        if (income.getClass() == CloudRequestServerStartPacket.class) {
            CloudRequestServerStartPacket packet = (CloudRequestServerStartPacket) income;
            ServerStarterQueue.getInstance().addServer(packet);
            return;
        }
        if (income.getClass() == CloudPlayerUpdatePacket.class) {
            CloudPlayerUpdatePacket packet = (CloudPlayerUpdatePacket) income;
            if (packet.isRemove()) CloudPlayerManager.getInstance().removePlayer(packet.getUUID());
            else CloudPlayerManager.getInstance().updatePlayer(packet.getCloudPlayer());

            return;
        }
        if (income.getClass() == CloudSendEventPacket.class) {
            CloudSendEventPacket packet = (CloudSendEventPacket) income;
            CloudServerManager.getInstance().getCloudServer().values().forEach(server -> {
                if (server.getNettyChannel() != null) {
                    if (!server.getServerUUID().toString().equals(packet.getSender().toString()))
                        server.writeToChannel(packet);
                }
            });
            return;
        }
        if (income.getClass() == CustomCloudMessagePacket.class) {
            CustomCloudMessagePacket packet = (CustomCloudMessagePacket) income;
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByName(packet.getTargetServer());
            if (cloudServer == null || cloudServer.getNettyChannel() == null) return;
            cloudServer.writeToChannel(packet);
            return;
        }
        if (income.getClass() == CloudPlayerSendServerPacket.class) {
            CloudPlayerSendServerPacket packet = (CloudPlayerSendServerPacket) income;
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByName(packet.getCloudPlayer().getProxy());
            cloudServer.writeToChannel(packet);
            return;
        }
        if (income.getClass() == CloudServerUpdateInfoPacket.class) {
            CloudServerUpdateInfoPacket packet = (CloudServerUpdateInfoPacket) income;
            CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).updateCloudServerInfo(packet.getCloudServerInfo());
            return;
        }

        CloudLogger.getInstance().log("[Error] An invalid packet was sent to the cloud: " + income.getClass());
    }

    private static void handleRequestPacket(Channel channel, Object object) {
        CloudRequestPacket packet = (CloudRequestPacket) object;
        if (packet.getRequestType() == CloudRequestType.SERVERS) {
            ArrayList<CloudServer> servers = new ArrayList<>(CloudServerManager.getInstance().getCloudServer().values());
            ArrayList<CloudServerObject> constructed = new ArrayList<>();
            servers.forEach(e -> constructed.add(e.constructObject()));
            channel.writeAndFlush(servers.get(0));
            channel.writeAndFlush(new CloudServerConstructedPacket(UUID.randomUUID(), servers.get(0)));
            channel.writeAndFlush(new CloudRespondServersPacket(packet.getRequestUUID(), constructed));
        }
        if (packet.getRequestType() == CloudRequestType.SERVER_INFO_UUID) {
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByUUID(UUID.fromString(packet.getKey()));
            CloudServerObject serverObject = cloudServer == null ? null : cloudServer.constructObject();
            channel.writeAndFlush(new CloudRespondServerInfoPacket(packet.getRequestUUID(), serverObject));
        }
        if (packet.getRequestType() == CloudRequestType.SERVER_INFO_NAME) {
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByName(packet.getKey());
            CloudServerObject serverObject = cloudServer == null ? null : cloudServer.constructObject();
            channel.writeAndFlush(new CloudRespondServerInfoPacket(packet.getRequestUUID(), serverObject));
        }
        if (packet.getRequestType() == CloudRequestType.TEMPLATES) {
            channel.writeAndFlush(new CloudRespondTemplatesPacket(packet.getRequestUUID(), new ArrayList<>(ServerTemplateManager.getInstance().getTemplates().values())));
        }
        if (packet.getRequestType() == CloudRequestType.PLAYER_INFO) {
            channel.writeAndFlush(new CloudRespondPlayerInfoPacket(packet.getRequestUUID(), CloudPlayerManager.getInstance().getPlayer(UUID.fromString(packet.getKey()))));
        }
        if (packet.getRequestType() == CloudRequestType.PLAYERS) {
            channel.writeAndFlush(new CloudRespondPlayersPacket(packet.getRequestUUID(), new ArrayList<>(CloudPlayerManager.getCloudPlayers().values())));
        }
        if (packet.getRequestType() == CloudRequestType.TEMPLATE_INFO) {
            channel.writeAndFlush(new CloudRespondTemplateInfoPacket(packet.getRequestUUID(), ServerTemplateManager.getInstance().getTemplates().get(packet.getKey())));
        }
    }
}
