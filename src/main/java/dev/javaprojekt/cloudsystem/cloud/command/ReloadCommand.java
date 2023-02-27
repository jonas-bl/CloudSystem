package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplateUpdatePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplatesReloadPacket;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.io.IOException;

public class ReloadCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        CloudLogger.getInstance().log("Reloading all templates...");
        try {
            ServerTemplateManager.getInstance().importTemplates();
        } catch (IOException e) {
            CloudLogger.getInstance().log("An error occured: " + e.getMessage());
            return;
        }
        CloudSlaveManager.getInstance().getSlaves().values().forEach(all -> {
            for (ServerTemplate template : ServerTemplateManager.getInstance().getTemplates().values()) {
                if (all.getChannel() != null) {
                    all.getChannel().writeAndFlush(new CloudTemplateUpdatePacket(template));
                }
            }
            all.getChannel().writeAndFlush(new CloudTemplatesReloadPacket());
        });
        CloudLogger.getInstance().log("Done!");
    }
}
