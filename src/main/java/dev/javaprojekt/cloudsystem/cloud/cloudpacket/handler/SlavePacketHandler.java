package dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStartPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerConstructedPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplateUpdatePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplatesReloadPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.slave.CloudSlave;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import io.netty.channel.Channel;

import java.io.IOException;
import java.util.UUID;

public class SlavePacketHandler {

    public static void handlePacket(Object income) {
        if (income.getClass() == CloudRequestServerStartPacket.class) {
            CloudRequestServerStartPacket packet = (CloudRequestServerStartPacket) income;
            ServerTemplate template = ServerTemplateManager.getInstance().getTemplates().get(packet.getServerTemplate());
            ServerTemplate copyTemplate = ServerTemplateManager.getInstance().getTemplates().get(packet.getCopyTemplate());
            if (template == null) {
                CloudLogger.getInstance().log("The requested server template '" + packet.getServerTemplate() + "' does not exist.");
                return;
            }
            CloudLogger.getInstance().log("Preparing new server for template " + template.getName());
            CloudServer cloudServer = CloudServerManager.getInstance().prepareAndStartServer(template, copyTemplate, packet.getServerUUID());
            if(cloudServer == null)return;
            CloudSlave.getSocketClient().getChannel().writeAndFlush(new CloudServerConstructedPacket(cloudServer.getServerUUID(), cloudServer));
            return;
        }
        if (income.getClass() == CloudRequestServerStopPacket.class) {
            CloudRequestServerStopPacket packet = (CloudRequestServerStopPacket) income;
            CloudServer server = CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID());
            if (server == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            CloudLogger.getInstance().log("Force stopping server " + server.getName() + "...");
            CloudServerManager.getInstance().forceStopServer(packet.getServerUUID());
            return;
        }
        if (income.getClass() == CloudServerStopPacket.class) {
            CloudServerStopPacket packet = (CloudServerStopPacket) income;
            CloudLogger.getInstance().log("Unregistering server " + CloudServerManager.getInstance().getServerByUUID(packet.getServerUUID()).getName() + "...");
            CloudServerManager.getInstance().unregisterServer(packet.getServerUUID());
        }
        if (income.getClass() == CloudTemplateUpdatePacket.class) {
            CloudTemplateUpdatePacket packet = (CloudTemplateUpdatePacket) income;
            ServerTemplateManager.getInstance().createTemplate(packet.getTemplate());
        }
        if(income.getClass() == CloudTemplatesReloadPacket.class) {
            try {
                ServerTemplateManager.getInstance().importTemplates();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
