package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStartPacket;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.*;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.util.UUID;

public class StartServerCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            ServerTemplate template = ServerTemplateManager.getInstance().getTemplates().get(args[0]);
            if (template == null) {
                CloudLogger.getInstance().log("This server template does not exist.");
                return;
            }
            ServerStarterQueue.getInstance().addServer(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), null, UUID.randomUUID()));
            return;
        }
        CloudLogger.getInstance().log("Please use: /startserver <template>");

    }
}
