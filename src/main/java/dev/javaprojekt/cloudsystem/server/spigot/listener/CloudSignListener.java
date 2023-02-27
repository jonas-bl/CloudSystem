package dev.javaprojekt.cloudsystem.server.spigot.listener;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSign;
import dev.javaprojekt.cloudsystem.server.spigot.sign.CloudSignManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CloudSignListener implements Listener {

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        CloudSign cloudSign = CloudSignManager.getInstance().getSignByLocation(event.getClickedBlock().getLocation());
        if (cloudSign == null || cloudSign.getConnectedServer() == null || CloudAPI.getInstance().getServer(cloudSign.getConnectedServer()).getServerState().equals(ServerState.OFFLINE))
            return;
        player.sendMessage("§a§oConnecting to " + cloudSign.getConnectedServer() + " ...");
        CloudAPI.getInstance().sendPlayer(CloudAPI.getInstance().getPlayer(player.getUniqueId()), cloudSign.getConnectedServer());
    }

    @EventHandler (ignoreCancelled = true)
    public void on(BlockBreakEvent event) {
        CloudSign cloudSign = CloudSignManager.getInstance().getSignByLocation(event.getBlock().getLocation());
        if(cloudSign == null)return;
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockPhysicsEvent event) {
        CloudSign cloudSign = CloudSignManager.getInstance().getSignByLocation(event.getBlock().getLocation());
        if(cloudSign == null)return;
        event.setCancelled(true);
    }
}
