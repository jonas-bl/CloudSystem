package dev.javaprojekt.cloudsystem.server.bungee.fakeplayers;

import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FakePlayerManager {

    private static int online = 0;
    private static int timer = 1;
    private static int count = 0;
    private static int id = 0;

    public static void setId(int id) {
        FakePlayerManager.id = id;
    }

    public static int getId() {
        return id;
    }

    public static void setOnline(int online) {
        FakePlayerManager.online = online;
    }

    private static ScheduledTask scheduledTask;

    public static ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    public static void startTask() {
       scheduledTask  = ProxyServer.getInstance().getScheduler().schedule(CloudBungee.getInstance(), new Runnable() {
            @Override
            public void run() {
                timer = getRandomInteger(6,30);
                int newOnline = getRandomInteger(online-2,online+3);
              //  if((online/10 >= 1) && online/10) {
                     newOnline = count >= 10 ? getRandomInteger(online-2,online+2) : getRandomInteger(online-2,online+1);
           //     }
                if(newOnline <= 0) {
                    newOnline = 0;
                }
                if(newOnline >= 120) {
                    newOnline = 120;
                }
                online = newOnline;

                int global = online+ProxyServer.getInstance().getOnlineCount();
                ProxyServer.getInstance().getPlayers().forEach(all -> {
                    all.setTabHeader(TextComponent.fromLegacyText("§cOnline: §7" +global + " Spieler" ), TextComponent.fromLegacyText("§cServer: §7" + all.getServer().getInfo().getName()));
                });
                count = count>= 10 ? 0 : +1;
            }
        },10,timer, TimeUnit.SECONDS);
    }

    public static void stopTask() {
        scheduledTask.cancel();
        scheduledTask = null;
    }

    public static int getOnline() {
        return online;
    }

    public static int getRandomInteger(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

}
