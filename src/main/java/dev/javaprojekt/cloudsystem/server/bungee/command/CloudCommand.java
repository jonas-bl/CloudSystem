package dev.javaprojekt.cloudsystem.server.bungee.command;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.server.Constants;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.bungee.fakeplayers.FakePlayerManager;
import dev.javaprojekt.cloudsystem.server.bungee.listener.PlayerListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class CloudCommand extends Command {

    public CloudCommand() {
        super("cloud", "", "cloudsystem", "nms");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cloudsystem.command")) {
            sender.sendMessage(Constants.PREFIX + "§bCloud System by JavaProjekt §f┃ §ev." + CloudBungee.getInstance().getDescription().getVersion());
            return;
        }

        if (args.length >= 2) {
            if (args[0].equals("command")) {
                CloudServerObject server = CloudAPI.getInstance().getServer(args[1]);
                if (server == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis server does not exist.");
                    return;
                }
                ArrayList<String> list = new ArrayList<>(Arrays.asList(args));
                list.remove(args[0]);
                list.remove(args[1]);
                String command = String.join(" ", list);
                CloudAPI.getInstance().sendCommand(server.getName(), command);
                sender.sendMessage(Constants.PREFIX + "§7The command was sent to §b" + server.getName() + "§7.");
                return;
            }
        }
        if (args.length == 2) {
            if (args[0].equals("list")) {
                if (args[1].equalsIgnoreCase("templates")) {
                    ArrayList<ServerTemplate> templates = CloudAPI.getInstance().getTemplates();
                    if (templates == null || templates.isEmpty()) {
                        sender.sendMessage(Constants.PREFIX + "§cThis sounds weird, but currently no templates exist.");
                        return;
                    }
                    sender.sendMessage(Constants.PREFIX + "§cOverview of all templates:");
                    templates.forEach(e -> {
                        sender.sendMessage(Constants.PREFIX + " §7- " + e.getName());
                    });
                    return;
                }
                if (args[1].equalsIgnoreCase("servers") || args[1].equalsIgnoreCase("server")) {
                    ArrayList<CloudServerObject> servers = CloudAPI.getInstance().getCloudServers();
                    if (servers == null || servers.isEmpty()) {
                        sender.sendMessage(Constants.PREFIX + "§cThis sounds weird, but currently no servers exist.");
                        return;
                    }
                    sender.sendMessage(Constants.PREFIX + "§cOverview of all online servers:");
                    servers.forEach(e -> {
                        String status = e.getServerState() == ServerState.OFFLINE ? "§cOFFLINE" : "§aONLINE";
                        sender.sendMessage(Constants.PREFIX + " §7- " + e.getName() + " §7(" + status + "§7)");
                    });
                    return;
                }
                sender.sendMessage(Constants.PREFIX + "§7- /list <templates / server> | Prints a list of the given category");
                return;
            }
            if (args[0].equals("startserver")) {
                ServerTemplate template = CloudAPI.getInstance().getTemplate(args[1]);
                if (template == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis template does not exist.");
                    return;
                }
                CloudAPI.getInstance().startServer(template);
                sender.sendMessage(Constants.PREFIX + "§7You started a §b" + template.getName() + " §7server");
                return;
            }
            if (args[0].equals("stopserver")) {
                CloudServerObject server = CloudAPI.getInstance().getServer(args[1]);
                if (server == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis server does not exist.");
                    return;
                }
                CloudAPI.getInstance().stopServer(server);
                sender.sendMessage(Constants.PREFIX + "§7You stopped the server §b" + server.getName());
                return;
            }
            if (args[0].equals("stopgroup")) {
                ServerTemplate template = CloudAPI.getInstance().getTemplate(args[1]);
                if (template == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis template does not exist.");
                    return;
                }
                CloudAPI.getInstance().getCloudServersByTemplate(template.getName()).forEach(e -> {
                    CloudAPI.getInstance().stopServer(e);
                });
                sender.sendMessage(Constants.PREFIX + "§7Stopping all servers of the template §b" + template.getName() + "§7.");
                return;
            }
            if (args[0].equals("serverinfo")) {
                CloudServerObject server = CloudAPI.getInstance().getServer(args[1]);
                if (server == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis server does not exist.");
                    return;
                }
                sender.sendMessage(Constants.PREFIX + "§eServer " + server.getName() + ":");
                sender.sendMessage(Constants.PREFIX + "  - UUID: " + server.getServerUUID());
                sender.sendMessage(Constants.PREFIX + "  - Template: " + server.getTemplate().getName());
                sender.sendMessage(Constants.PREFIX + "  - Ram: " + server.getRam());
                sender.sendMessage(Constants.PREFIX + "  - Address: " + server.getAdress());
                sender.sendMessage(Constants.PREFIX + "  - Port: " + server.getPort());
                sender.sendMessage(Constants.PREFIX + "  - ServerState: " + server.getServerState().name());
                return;
            }
            if (args[0].equals("templateinfo")) {
                ServerTemplate template = CloudAPI.getInstance().getTemplate(args[1]);
                if (template == null) {
                    sender.sendMessage(Constants.PREFIX + "§cThis template does not exist.");
                    return;
                }
                sender.sendMessage(Constants.PREFIX + "§eTemplate " + template.getName() + ":");
                sender.sendMessage(Constants.PREFIX + "  - Type: " + template.getServerType().name());
                sender.sendMessage(Constants.PREFIX + "  - Version: " + template.getServerVersion().name());
                sender.sendMessage(Constants.PREFIX + "  - Ram: " + template.getRam());
                sender.sendMessage(Constants.PREFIX + "  - MinOnline: " + template.getMinOnline());
                sender.sendMessage(Constants.PREFIX + "  - MaxOnline: " + template.getMaxOnline());
                sender.sendMessage(Constants.PREFIX + "  - Slave: " + template.getSlave());
                return;
            }
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("fakeplayers")) {
                if(FakePlayerManager.getScheduledTask() == null) {
                    sender.sendMessage(Constants.PREFIX + "§aFake Players are now enabled.");
                    FakePlayerManager.startTask();
                    return;
                }
                sender.sendMessage(Constants.PREFIX + "§cFake Players are now disabled.");
                FakePlayerManager.stopTask();
                FakePlayerManager.setOnline(0);
                return;
            }
            if(args[0].equalsIgnoreCase("nokick")) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                if(PlayerListener.getNoKick().contains(player)) {
                    PlayerListener.getNoKick().remove(player);
                    player.sendMessage(Constants.PREFIX + "§cYou will now be kicked normal.");
                    return;
                }
                PlayerListener.getNoKick().add(player);
                player.sendMessage(Constants.PREFIX + "§cYou will now bypass kicks");
                return;
            }
        }

        sender.sendMessage(Constants.PREFIX + "§bCloudSystem §ev." + CloudBungee.getInstance().getDescription().getVersion() + " §bby JavaProjekt (Jonas B.)");
        sender.sendMessage(Constants.PREFIX + "§bCloudsystem command overview:");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud list <templates / servers> | Prints a list of the given category");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud startserver <template> | Start a server of the given template");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud stopserver <server> | Stop a running server");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud serverinfo <server> | Prints information about a server");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud templateinfo <template> | Prints information about a template");
        sender.sendMessage(Constants.PREFIX + "§7- /cloud command <server> <command ...> | Execute a command on a server");
    }
}
