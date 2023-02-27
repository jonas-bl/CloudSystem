package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStartPacket;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerStarterQueue {

    private static ServerStarterQueue instance;

    private static boolean stopping = false;

    public static boolean isStopping() {
        return stopping;
    }

    public static void setStopping(boolean stopping) {
        ServerStarterQueue.stopping = stopping;
    }

    public static ServerStarterQueue getInstance() {
        return instance;
    }

    public ServerStarterQueue() {
        instance = this;
        requests = new HashMap<>();
        starting = new HashMap<>();
    }

    private HashMap<UUID, CloudRequestServerStartPacket> requests;
    private HashMap<UUID, CloudRequestServerStartPacket> starting;

    public HashMap<UUID, CloudRequestServerStartPacket> getRequests() {
        return requests;
    }

    public HashMap<UUID, CloudRequestServerStartPacket> getStarting() {
        return starting;
    }

    public void addServer(CloudRequestServerStartPacket packet) {
        requests.put(packet.getServerUUID(), packet);
    }

    public void start() {
        Thread thread = new Thread(() -> {
            final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
            ses.scheduleWithFixedDelay(() -> {
                if (stopping) {
                    ses.shutdown();
                    return;
                }
                addDefaults();
                if (!requests.isEmpty()) {
                    if(starting.size() >= 5)return;
                    ArrayList<UUID> list = new ArrayList<>(requests.keySet());
                    list = (ArrayList<UUID>) list.clone();
                    for (UUID uuid : list) {
                        CloudRequestServerStartPacket packet = requests.get(uuid);
                        CloudSlaveObject cloudSlaveObject = CloudSlaveManager.getInstance().getSlaveByName(packet.getTargetSlave());
                        if (cloudSlaveObject == null) {
                            CloudLogger.getInstance().log("ERROR: The slave " + packet.getTargetSlave() + " does not exist! Please check " + packet.getServerTemplate()+ ".json");
                            continue;
                        }
                        if (cloudSlaveObject.getChannel() == null) {
                            continue;
                        }
                        requests.remove(uuid);
                        int takenRam = CloudServerManager.getInstance().getUsedRam(packet.getTargetSlave());
                        ServerTemplate template = packet.getCopyTemplate() != null ? ServerTemplateManager.getInstance().getTemplates().get(packet.getCopyTemplate()) : ServerTemplateManager.getInstance().getTemplates().get(packet.getServerTemplate());
                        takenRam += template.getRam();
                        if( takenRam>=  CloudSlaveManager.getInstance().getSlaveByName(template.getSlave()).getMaxRam()){
                            requests.put(uuid, packet);
                            CloudLogger.getInstance().log("ERROR: Cloud not start a new " + template.getName() + " server because the Slave does not have enough RAM.");
                            continue;
                        }
                        cloudSlaveObject.getChannel().writeAndFlush(packet);
                        CloudLogger.getInstance().log("Told " + cloudSlaveObject.getName() + " to start a " + template.getName() + " server.");
                        starting.put(uuid, packet);
                        break;
                    }
                }
            }, 5, 2, TimeUnit.SECONDS);
        });
        thread.start();
    }

    public void removeStarting(UUID uuid) {
        starting.remove(uuid);
    }

    public int getQueueTemplateAmount(String template) {
        int i = 0;
        for (CloudRequestServerStartPacket e : requests.values()) {
            if (e.getServerTemplate().equals(template)) i++;
        }
        return i;
    }

    public int getQueueStartingAmount(String template) {
        int i = 0;
        for (CloudRequestServerStartPacket e : starting.values()) {
            if (e.getServerTemplate().equals(template)) i++;
        }
        return i;
    }

    public void addDefaults() {
        for (ServerTemplate template : ServerTemplateManager.getInstance().getTemplates().values()) {
            int queue = getQueueTemplateAmount(template.getName());
            int starting = getQueueStartingAmount(template.getName());
            int online = CloudServerManager.getInstance().getServersByTemplate(template.getName()).size();
            if (queue >= template.getMinOnline()) continue;
            if (starting >= template.getMinOnline()) continue;
            if (online >= template.getMinOnline()) continue;
            int total = starting + queue + online;
            if(total >= template.getMinOnline())continue;
          //  while ((getQueueTemplateAmount(template.getName()) < template.getMinOnline())) {
            while (total < template.getMinOnline()) {
                CloudLogger.getInstance().log("Added a " + template.getName() + " to the queue");
                addServer(new CloudRequestServerStartPacket(template.getSlave(), template.getName(), null, UUID.randomUUID()));
                total++;
            }
        }
    }
}
