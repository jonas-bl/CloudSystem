package dev.javaprojekt.cloudsystem.server.bungee.command;

import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayer;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessage;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.bungee.fakeplayers.FakePlayerManager;
import dev.javaprojekt.cloudsystem.server.bungee.lobbychoose.LobbyChooser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand() {
        super("hub", "", "lobby", "l");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if(args.length == 1) {
            Integer integer = Integer.parseInt(args[0]);
            FakePlayerManager.setId(integer);
            player.sendMessage("New ID: " + FakePlayerManager.getId());
            return;
        }

        if (player.getServer().getInfo().getName().startsWith("Lobby-")) {
            player.sendMessage("§cYou are already connected to a hub.");
            return;
        }
        ServerInfo serverInfo = new LobbyChooser().getFreeLobby();
        if (serverInfo == null) {
            player.sendMessage("§cCurrently is no free hub server available.");
            return;
        }
        player.sendMessage("§a§oConnecting to " + serverInfo.getName() + "...");
        player.connect(serverInfo);

    }
}
