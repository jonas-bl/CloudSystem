package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerForceStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.commander.CloudCommander;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class StopServerCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            CloudServer server = CloudServerManager.getInstance().getServerByName(args[0]);
            if (server == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            CloudLogger.getInstance().log("Stopping server " + server.getName() + "...");
            if (server.getNettyChannel() == null) {
                CloudSlaveManager.getInstance().getSlaveByName(server.getTemplate().getSlave()).getChannel().writeAndFlush(new CloudServerForceStopPacket(server.getServerUUID()));
                CloudCommander.getSocketServer().getChannel().writeAndFlush(new CloudServerStopPacket(server.getServerUUID()));
                CloudServerManager.getInstance().forceStopServer(server.getServerUUID());
                CloudLogger.getInstance().log("Notice: Force stopped because Netty Channel is invalid.");
                return;
            }
            server.getNettyChannel().writeAndFlush(new CloudRequestServerStopPacket(server.getServerUUID()));
            return;
        }
        if (args.length == 2) {
            CloudServer server = CloudServerManager.getInstance().getServerByName(args[0]);
            if (server == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            CloudLogger.getInstance().log("Requested Force Stopping server " + server.getName() + "...");
            CloudServerManager.getInstance().forceStopServer(server.getServerUUID());
            return;
        }
        CloudLogger.getInstance().log("Please use: /stopserver <server>");
    }
}
