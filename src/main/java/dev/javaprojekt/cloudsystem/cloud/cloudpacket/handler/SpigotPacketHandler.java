package dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.event.CloudSendEventPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.CustomCloudMessagePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond.*;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerCommandPacket;
import dev.javaprojekt.cloudsystem.cloud.util.future.AsyncFutureRequest;
import dev.javaprojekt.cloudsystem.event.CloudEventManager;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessageEvent;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudConnectEvent;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import org.bukkit.Bukkit;

public class SpigotPacketHandler {

    public static void handlePacket(Object object) {
        if (object.getClass() == CloudRequestServerStopPacket.class) {
            CloudRequestServerStopPacket packet = (CloudRequestServerStopPacket) object;
            Bukkit.shutdown();
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
        if (object.getClass() == CloudServerCommandPacket.class) {
            CloudServerCommandPacket packet = (CloudServerCommandPacket) object;
            Bukkit.getScheduler().runTaskLater(CloudSpigot.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), packet.getCommand());
                }
            }, 1L);
        }
    }
}
