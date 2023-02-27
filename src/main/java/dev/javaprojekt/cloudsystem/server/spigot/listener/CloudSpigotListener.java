package dev.javaprojekt.cloudsystem.server.spigot.listener;

import dev.javaprojekt.cloudsystem.event.CloudEventHandler;
import dev.javaprojekt.cloudsystem.event.CloudListener;
import dev.javaprojekt.cloudsystem.event.message.CustomCloudMessageEvent;
import org.bukkit.Bukkit;

public class CloudSpigotListener implements CloudListener {

    @CloudEventHandler
    public void on(CustomCloudMessageEvent event) {

    }
}
