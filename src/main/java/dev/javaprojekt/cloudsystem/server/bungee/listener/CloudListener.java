package dev.javaprojekt.cloudsystem.server.bungee.listener;

import dev.javaprojekt.cloudsystem.event.CloudEventHandler;
import dev.javaprojekt.cloudsystem.event.player.PlayerCloudConnectEvent;

public class CloudListener implements dev.javaprojekt.cloudsystem.event.CloudListener {

    @CloudEventHandler
    public void onConnect(PlayerCloudConnectEvent event) {
        System.out.println("Player " + event.getCloudPlayer().getName() + " connected to the cloud!");
    }

}
