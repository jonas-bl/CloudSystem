package dev.javaprojekt.cloudsystem.server.spigot.sign;

import com.google.common.base.Splitter;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class CloudSignManager {

    private static CloudSignManager instance;

    private int animationCount = 0;

    private ArrayList<CloudSign> signs = new ArrayList<>();

    public CloudSignManager() {
        instance = this;
    }

    public String getAnimationByCount(int count) {
        if(count == 0)return "┃";
        if(count == 1)return "/";
        if(count == 2)return "─";
        if(count == 3)return "\\";
        if(count == 4)return "┃";
        if(count == 5)return "/";
        if(count == 6)return "─";
        if(count == 7)return "\\";
        return "...";

    }

    public static CloudSignManager getInstance() {
        return instance;
    }

    public CloudSign getSignByLocation(Location location) {
        for (CloudSign all : getSigns()) {
            if (all.getSignLocation().equals(location)) return all;
        }
        return null;
    }

    public ArrayList<String> getTemplates() {
        ArrayList<String> list = new ArrayList<>();
        signs.forEach(e -> {
            if (!list.contains(e.getTemplate())) list.add(e.getTemplate());
        });
        return list;
    }

    public String getFreeServer(String template) {
        ArrayList<String> server = CloudAPI.getInstance().getCloudServersByTemplateString(template, "LOBBY");
        for (CloudSign sign : getSigns(template)) {
            server.remove(sign.getConnectedServer());
        }
        if (server.isEmpty()) return null;
        return server.get(0);
    }

    public ArrayList<CloudSign> getSigns(String template) {
        ArrayList<CloudSign> list = new ArrayList<>();
        signs.forEach(e -> {
            if (e.getTemplate() != null) {
                if (e.getTemplate().equals(template)) list.add(e);
            }
        });
        return list;
    }

    public ArrayList<CloudSign> getSigns() {
        return signs;
    }

    public void createSign(String template, Location location) {
        Sign sign = (Sign) location.getBlock().getState();
        String id = generateSignId();
        CloudSpigot.getInstance().getConfig().set("Signs." + id + ".Template", template);
        CloudSpigot.getInstance().getConfig().set("Signs." + id + ".Location", location);
        CloudSpigot.getInstance().saveConfig();
        getSigns().add(new CloudSign(id, template, location));
        sign.setLine(0, "[" + template + "]");
        sign.setLine(1, " ");
        sign.setLine(2, "Loading...");
        sign.setLine(3, " ");
        sign.update();
    }

    public void removeSign(CloudSign cloudSign) {
        Sign sign = (Sign) cloudSign.getSignLocation().getBlock().getState();
        sign.setLine(0, "");
        sign.setLine(1, "§cCloud Sign");
        sign.setLine(2, "Removed!");
        sign.setLine(3, "");
        sign.update();
        Bukkit.getScheduler().runTaskLater(CloudSpigot.getInstance(), new Runnable() {
            @Override
            public void run() {
                cloudSign.getSignLocation().getBlock().setType(Material.AIR);
                cloudSign.getSignLocation().getWorld().dropItem(cloudSign.getSignLocation(), new ItemStack(Material.SIGN));
            }
        },20L);
        CloudSpigot.getInstance().getConfig().set("Signs." + cloudSign.getId(), null);
        CloudSpigot.getInstance().saveConfig();
        getSigns().remove(cloudSign);

    }

    public void importSign(String id) {
        CloudSign cloudSign = new CloudSign(id, CloudSpigot.getInstance().getConfig().getString("Signs." + id + ".Template"),
                (Location) CloudSpigot.getInstance().getConfig().get("Signs." + id + ".Location", Location.class) );
        getSigns().add(new CloudSign(id, cloudSign.getTemplate(), cloudSign.getSignLocation()));
        Block block = cloudSign.getSignLocation().getBlock();
        if(block.getType() != null && block.getType().equals(Material.WALL_SIGN)) {
            Sign sign = (Sign) cloudSign.getSignLocation().getBlock().getState();
            sign.setLine(0, "[" + cloudSign.getTemplate() + "]");
            sign.setLine(1, " ");
            sign.setLine(2, "Loading...");
            sign.setLine(3, " ");
            sign.update();
        }
    }

    public void importSigns() {
        if (CloudSpigot.getInstance().getConfig().get("Signs") == null) return;
        CloudSpigot.getInstance().getConfig().getConfigurationSection("Signs").getKeys(false).forEach(this::importSign);
    }

    public void startTask() {
        getSigns().clear();
        importSigns();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CloudSpigot.getInstance(), new Runnable() {
            @Override
            public void run() {
                updateSigns();
            }
        },60,10L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CloudSpigot.getInstance(), new Runnable() {
            @Override
            public void run() {
                updateFreeSigns();
                animationCount++;
                if(animationCount > 7) {
                    animationCount = 0;
                }
            }
        },60,5L);
    }

    private void updateFreeSigns() {
        for (CloudSign cloudSign : getSigns()) {
            Block block = cloudSign.getSignLocation().getBlock();
            if(block.getType() == null || !block.getType().equals(Material.WALL_SIGN)) {
                continue;
            }
            if (cloudSign.getConnectedServer() != null) {
                continue;
            }
            updateSign(cloudSign);
        }
    }

    public void updateSign(CloudSign cloudSign) {
        Sign sign = (Sign) cloudSign.getSignLocation().getBlock().getState();
        if (cloudSign.getConnectedServer() == null) {
            sign.setLine(0, "[" + cloudSign.getTemplate() + "]");
            sign.setLine(1, " ");
            sign.setLine(2, "Loading...");
            sign.setLine(3, getAnimationByCount(animationCount));
        } else {
            CloudServerObject serverObject = CloudAPI.getInstance().getServer(cloudSign.getConnectedServer());
            if(serverObject.getServerState().equals(ServerState.OFFLINE)) {
                sign.setLine(0, "[" + cloudSign.getConnectedServer() + "]");
                sign.setLine(1, " ");
                sign.setLine(2, "§oStarting...");
                sign.setLine(3, " ");
            }else {
                sign.setLine(0, "[" + cloudSign.getConnectedServer() + "]");
                sign.setLine(1, getServerStatus(serverObject));
                sign.setLine(2, serverObject.getCloudServerInfo().getOnlinePlayers().size() + "/" + serverObject.getCloudServerInfo().getMaxPlayers());
                Iterable<String> pieces = Splitter.fixedLength(16).split(serverObject.getCloudServerInfo().getExtra());
                sign.setLine(3, pieces.iterator().next());
            }
        }
        sign.update();
    }

    public String getServerStatus(CloudServerObject serverObject) {
        String string = serverObject.getCloudServerInfo().getServerState();
        String status = string.equalsIgnoreCase("INGAME") ? "§c" + string : "§a" + string;
        status = (serverObject.getCloudServerInfo().getOnlinePlayers().size() >= serverObject.getCloudServerInfo().getMaxPlayers()) && string.equalsIgnoreCase("Lobby") ? "§6Premium" : status;
        return status;
    }

    public String generateSignId() {
        String signID = UUID.randomUUID().toString().split("-")[0];
        if (CloudSpigot.getInstance().getConfig().get(signID) != null) {
            generateSignId();
        }
        return signID;
    }

    public void updateSigns() {
        for (CloudSign cloudSign : getSigns()) {
            Block block = cloudSign.getSignLocation().getBlock();
            CloudServerObject serverObject = CloudAPI.getInstance().getServer(cloudSign.getConnectedServer());
            if(block.getType() == null || !block.getType().equals(Material.WALL_SIGN)) {
                continue;
            }
            if (cloudSign.getConnectedServer() == null) {
                String server = getFreeServer(cloudSign.getTemplate());
                if (server != null) {
                    cloudSign.setConnectedServer(server);
                }
            } else if (serverObject == null || !serverObject.getCloudServerInfo().getServerState().equalsIgnoreCase("LOBBY")) {
                cloudSign.setConnectedServer(null);
                System.out.println("Set connected server to null:");
                if(serverObject != null)
                System.out.println("State: " + serverObject.getCloudServerInfo().getServerState());
            }
            updateSign(cloudSign);
        }
    }
}
