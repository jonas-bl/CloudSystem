package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.commander.CloudCommander;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class StopNettyCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        CloudLogger.getInstance().log("Asked to stop netty server...");
        CloudCommander.getSocketServer().stop();
        CloudLogger.getInstance().log("Stopped server and closed connection..");
    }
}
