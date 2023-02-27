package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.ServerStarterQueue;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class ClearQueueCommand extends ConsoleCommand {
    @Override
    public void onCommand(String[] args) {
        ServerStarterQueue.getInstance().getStarting().clear();
        ServerStarterQueue.getInstance().getRequests().clear();
        CloudLogger.getInstance().log("Cleared the queue.");
    }
}
