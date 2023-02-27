package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.IOException;

public class CopyServerCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {

        if (args.length == 1) {
            CloudServer cloudServer = CloudServerManager.getInstance().getServerByName(args[0]);
            if (cloudServer == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            if (cloudServer.isStatic()) {
                CloudLogger.getInstance().log("A static server cannot be copied.");
                return;
            }
            try {
                FileManager.cleanDirectory(FileManager.getTemplatesDirectory(cloudServer.getTemplate().getName()));
                FileManager.copyFolder(FileManager.getTempDirectory(cloudServer.getTempName()).toPath(), FileManager.getTemplatesDirectory(cloudServer.getTemplate().getName()).toPath());
                CloudLogger.getInstance().log("The server was sucessfully copied to the template directory.");
            } catch (IOException e) {
                CloudLogger.getInstance().log("Error: Could not copy server!");
            }
            return;
        }

        CloudLogger.getInstance().log("Please use: /copyserver <Server>");
    }
}
