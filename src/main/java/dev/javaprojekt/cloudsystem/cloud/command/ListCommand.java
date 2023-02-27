package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class ListCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (args.length != 1) {
            CloudLogger.getInstance().log("Please use: list <templates / static / server>");
            return;
        }
        if (args[0].equalsIgnoreCase("templates")) {
            CloudLogger.getInstance().log("Overview of all templates:");
            ServerTemplateManager.getInstance().getTemplates().keySet().forEach(template -> {
                CloudLogger.getInstance().log("- " + template);
            });
            CloudLogger.getInstance().log(" ");
            return;
        }
        if (args[0].equalsIgnoreCase("server")) {
            CloudLogger.getInstance().log("Overview of all online servers:");
            CloudServerManager.getInstance().getCloudServer().values().forEach(server -> {
                CloudLogger.getInstance().log("- " + server.getName() + " (" + server.getServerState().name() + ")");
            });
            CloudLogger.getInstance().log(" ");
            return;
        }
    }
}
