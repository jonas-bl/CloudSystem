package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.ServerStarterQueue;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class QueueCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        CloudLogger.getInstance().log("Starting Queue Overview:");
        CloudLogger.getInstance().log("Requests: " + ServerStarterQueue.getInstance().getRequests().size());
        CloudLogger.getInstance().log("Starting: " + ServerStarterQueue.getInstance().getStarting().size());
    }
}
