package dev.javaprojekt.cloudsystem.server.spigot.command;

import dev.javaprojekt.cloudsystem.server.Constants;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSign;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSignManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.xml.sax.Locator;

import java.util.Set;

public class CloudSignCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Constants.PREFIX + "§cThis command can only by executed by players.");
            return true;
        }
        if (!sender.hasPermission("cloudsystem.cloudsign")) {
            sender.sendMessage(Constants.NOPERM_SPIGOT);
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                String template = args[1];
                if (CloudAPI.getInstance().getTemplate(template) == null) {
                    player.sendMessage(Constants.PREFIX + "§cThis template does not exist.");
                    return true;
                }
                Block block =  player.getTargetBlock((Set<Material>) null, 5);
                if(block.getType() == null || !block.getType().equals(Material.WALL_SIGN)) {
                    player.sendMessage(Constants.PREFIX + "§cYou must look at a sign to create a new one.");
                    return true;
                }
                CloudAPI.getInstance().getCloudSignManager().createSign(template, block.getLocation());
                player.sendMessage(Constants.PREFIX + "§aYou created a new sign for the template §b" + template + "§a.");
                return true;
            }
        }
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("update")) {
                CloudSignManager.getInstance().updateSigns();
                player.sendMessage(Constants.PREFIX + "§aYou updated all signs.");
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                Block block =  player.getTargetBlock((Set<Material>) null, 5);
                if(block.getType() == null || !block.getType().equals(Material.WALL_SIGN)) {
                    player.sendMessage(Constants.PREFIX + "§cYou must look at a sign to create a new one.");
                    return true;
                }
                CloudSign cloudSign = CloudSignManager.getInstance().getSignByLocation(block.getLocation());
                if(cloudSign == null) {
                    player.sendMessage(Constants.PREFIX + "§cNo sign exists at this location.");
                    return true;
                }
                CloudSignManager.getInstance().removeSign(cloudSign);
                player.sendMessage(Constants.PREFIX + "§aYou removed a §b" + cloudSign.getTemplate() + " §asign.");
                return true;
            }
        }

        player.sendMessage(Constants.PREFIX + "§bCloudSystem §ev." + CloudSpigot.getInstance().getDescription().getVersion() + " §bby JavaProjekt (Jonas B.)");
        player.sendMessage(Constants.PREFIX + "§bCloud Sign command overview:");
        player.sendMessage(Constants.PREFIX + "§7- /cloudsign create <template> | Creates a new sign for the given template");
        player.sendMessage(Constants.PREFIX + "§7- /cloudsign update | Update all signs");
        player.sendMessage(Constants.PREFIX + "§7- /cloudsign delete | Deletes the sign of the currently faced block");
        player.sendMessage(Constants.PREFIX);
        return false;
    }
}
