package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class ServerInfoCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if(args.length == 1) {
            CloudServer server = CloudServerManager.getInstance().getServerByName(args[0]);
            if(server == null) {
                CloudLogger.getInstance().log("This server does not exist!");
                return;
            }
            CloudLogger.getInstance().log("- " + server.getName());
            CloudLogger.getInstance().log("  - UUID: " + server.getServerUUID());
            CloudLogger.getInstance().log("  - Template: " + server.getTemplate());
            CloudLogger.getInstance().log("  - Ram: " + server.getRam());
            CloudLogger.getInstance().log("  - Port: " + server.getPort());
            CloudLogger.getInstance().log("  - ServerState: " + server.getServerState().name());
            return;
        }
        CloudLogger.getInstance().log("Please use: /serverinfo <server>");
    }
}
