package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerCommandPacket;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.util.ArrayList;
import java.util.Arrays;

public class CMDCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (args.length >= 2) {
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByName(args[0]);
            if (cloudServer == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
            list.remove(args[0]);
            String command = String.join(" ", list);
            cloudServer.getNettyChannel().writeAndFlush(new CloudServerCommandPacket(cloudServer.getServerUUID(), command));
            CloudLogger.getInstance().log(" The command was sent to " + cloudServer.getName());
            return;
        }
        CloudLogger.getInstance().log("Please use: /sendcommmand <server> <command ...>");
    }
}
