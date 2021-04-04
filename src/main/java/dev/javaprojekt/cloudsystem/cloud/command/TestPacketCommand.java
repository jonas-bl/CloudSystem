package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class TestPacketCommand extends ConsoleCommand {
    @Override
    public void onCommand(String[] args) {
        CloudLogger.getInstance().log("");
    }
}
