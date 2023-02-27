package dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.event.CloudSendEventPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CloudNotifyPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CustomCloudMessagePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.enums.CloudNotifyType;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.player.CloudPlayerSendServerPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond.*;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerAddPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerRemovePacket;
import dev.javaprojekt.cloudsystem.cloud.util.future.AsyncFutureRequest;
import dev.javaprojekt.cloudsystem.event.CloudEventManager;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessageEvent;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudConnectEvent;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import io.netty.channel.Channel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;

public class BungeePacketHandler {

    public static void handlePacket(Channel channel, Object object) {
        if (object.getClass() == CloudServerRemovePacket.class) {
            CloudServerRemovePacket packet = (CloudServerRemovePacket) object;
            ProxyServer.getInstance().getServers().remove(packet.getServerName());
        }
        if (object.getClass() == CloudServerAddPacket.class) {
            CloudServerAddPacket packet = (CloudServerAddPacket) object;
            ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(packet.getServerName(), new InetSocketAddress(packet.getAdress(), packet.getPort()), "", false);
            ProxyServer.getInstance().getServers().put(packet.getServerName(), serverInfo);
        }
        if (object.getClass() == CloudRequestServerStopPacket.class) {
            CloudRequestServerStopPacket packet = (CloudRequestServerStopPacket) object;
            ProxyServer.getInstance().stop();
        }
        if (object.getClass() == CloudNotifyPacket.class) {
            CloudNotifyPacket packet = (CloudNotifyPacket) object;
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.hasPermission("cloudsystem.notify")) {
                    if (packet.getCloudNotifyType() == CloudNotifyType.SERVER_STARTED) {
                        player.sendMessage("§b[CloudSystem] §aThe server §b" + packet.getValue() + " §ais now online.");
                    }
                    if (packet.getCloudNotifyType() == CloudNotifyType.SERVER_STOPPED) {
                        player.sendMessage("§b[CloudSystem] §cThe server §b" + packet.getValue() + " §cis now offline.");
                    }
                }
            }
        }
        if (object.getClass() == CloudRespondServerInfoPacket.class) {
            CloudRespondServerInfoPacket packet = (CloudRespondServerInfoPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getCloudServer());
        }
        if (object.getClass() == CloudRespondServersPacket.class) {
            CloudRespondServersPacket packet = (CloudRespondServersPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getServers());
        }
        if (object.getClass() == CloudRespondTemplateInfoPacket.class) {
            CloudRespondTemplateInfoPacket packet = (CloudRespondTemplateInfoPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getServerTemplate());
        }
        if (object.getClass() == CloudRespondTemplatesPacket.class) {
            CloudRespondTemplatesPacket packet = (CloudRespondTemplatesPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getTemplates());
        }
        if (object.getClass() == CloudRespondPlayerInfoPacket.class) {
            CloudRespondPlayerInfoPacket packet = (CloudRespondPlayerInfoPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getCloudPlayer());
        }
        if (object.getClass() == CloudRespondPlayersPacket.class) {
            CloudRespondPlayersPacket packet = (CloudRespondPlayersPacket) object;
            AsyncFutureRequest.getRequestResponds().put(packet.getRequestUUID(), packet.getCloudPlayers());
        }
        if (object.getClass() == CloudSendEventPacket.class) {
            final CloudSendEventPacket packet = (CloudSendEventPacket)object;
            CloudAPI.getInstance().getEventManager().callEvent(packet.getEvent(), false);
        }
        if(object.getClass() == CustomCloudMessagePacket.class) {
            CustomCloudMessagePacket packet = (CustomCloudMessagePacket)object;
            CloudAPI.getInstance().getEventManager().callEvent(new CustomCloudMessageEvent(packet.getCloudMessage()), false);
        }
        if(object.getClass() == CloudPlayerSendServerPacket.class) {
            CloudPlayerSendServerPacket packet = (CloudPlayerSendServerPacket)object;
            if(!ProxyServer.getInstance().getServers().containsKey(packet.getServer()))return;
            ProxyServer.getInstance().getPlayer(packet.getCloudPlayer().getUUID()).connect(ProxyServer.getInstance().getServerInfo(packet.getServer()));
        }
    }
}
