package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class EndCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        CloudLogger.getInstance().log("Asked to shutdown system...");
        System.exit(0);
    }
}
