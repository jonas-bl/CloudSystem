package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplateUpdatePacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.template.CloudTemplatesReloadPacket;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveConfigObject;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.io.IOException;

public class CreateCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {
        if (args.length == 6) {
            if (args[0].equalsIgnoreCase("template") || args[0].equalsIgnoreCase("static")) {
                ServerType type = ServerType.valueOf(args[0].toUpperCase());
                String name = args[1];
                if (args[2].equalsIgnoreCase("spigot") || args[2].equalsIgnoreCase("proxy")) {
                    ServerVersion version = ServerVersion.valueOf(args[2].toUpperCase());
                    try {
                        int ram = Integer.parseInt(args[3]);
                        int maxOnline = Integer.parseInt(args[4]);
                        String slave = args[5];
                        CloudSlaveObject cloudSlaveObject = CloudSlaveManager.getInstance().getSlaveByName(slave);
                        if (cloudSlaveObject == null) {
                            CloudLogger.getInstance().log("This slave does not exist.");
                            return;
                        }
                        ServerTemplate template = new ServerTemplate(name, type, version, ram, 1, maxOnline, cloudSlaveObject.getName());
                        ServerTemplateManager.getInstance().createTemplate(template);
                        CloudLogger.getInstance().log("Created template " + template.getName() + " successfully!");
                        ServerTemplateManager.getInstance().importTemplates();
                        CloudSlaveManager.getInstance().getSlaves().values().forEach(e -> {
                            if (e.getChannel() != null) {
                                e.getChannel().writeAndFlush(new CloudTemplateUpdatePacket(template));
                                e.getChannel().writeAndFlush(new CloudTemplatesReloadPacket());
                            }
                        });
                        return;
                    } catch (NumberFormatException e) {
                        CloudLogger.getInstance().log("You must specify a number for 'ram' and 'maxonline'.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                CloudLogger.getInstance().log("You must select 'SPIGOT' or 'PROXY' as a server version.");
                return;
            }
            CloudLogger.getInstance().log("You must select 'TEMPLATE' or 'STATIC' as a server type.");
            return;
        }
        if (args.length == 4) {
            if (args[0].equals("slave")) {
                CloudSlaveObject slave = CloudSlaveManager.getInstance().getSlaveByName(args[0]);
                if (slave != null) {
                    CloudLogger.getInstance().log("This slave already exists.");
                    return;
                }
                try {
                    int ram = Integer.parseInt(args[3]);
                    CloudSlaveConfigObject cloudSlaveObject = new CloudSlaveConfigObject(args[1], ram, args[2]);
                    CloudSlaveManager.getInstance().createSlave(cloudSlaveObject);
                    CloudSlaveManager.getInstance().importSlaves();
                    CloudLogger.getInstance().log("Created slave " + cloudSlaveObject.getName() + " successfully!");
                } catch (NumberFormatException e) {
                    CloudLogger.getInstance().log("You must specify a number for 'ram'.");
                }
                return;
            }
        }
        CloudLogger.getInstance().log("Syntax for create:");
        CloudLogger.getInstance().log("- create template <name> <spigot / proxy> <ram> <maxonline> <slave>");
        CloudLogger.getInstance().log("- create static <name> <spigot / proxy> <ram> <maxonline> <slave>");
        CloudLogger.getInstance().log("- create slave <name> <adress> <ram>");
    }
}
