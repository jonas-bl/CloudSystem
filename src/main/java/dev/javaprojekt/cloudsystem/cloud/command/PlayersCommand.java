package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayerManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class PlayersCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (CloudPlayerManager.getCloudPlayers().isEmpty()) {
            CloudLogger.getInstance().log("Currently are no players connected.");
            return;
        }
        CloudLogger.getInstance().log("Online Players:");
        CloudPlayerManager.getCloudPlayers().values().forEach(e -> {
            CloudLogger.getInstance().log("- " + e.getName());
        });
    }
}
