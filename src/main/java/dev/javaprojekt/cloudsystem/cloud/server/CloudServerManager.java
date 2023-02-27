package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import sun.java2d.loops.ProcessPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CloudServerManager {

    private HashMap<UUID, CloudServer> cloudServer = new HashMap<>();
    private HashMap<UUID, Process> serverProcess = new HashMap<>();

    public HashMap<UUID, Process> getServerProcess() {
        return serverProcess;
    }

    public void forceStopServer(UUID uuid) {
        ServerStarterQueue.getInstance().removeStarting(uuid);
        ServerStarterQueue.getInstance().getRequests().remove(uuid);
        Process process = serverProcess.get(uuid);
        CloudServer cloudServer = CloudServerManager.getInstance().getServerByUUID(uuid);
        if(cloudServer.getTemplate().getServerVersion().equals(ServerVersion.PROXY)) {
            CloudServerManager.getInstance().getUsedProxyPorts().remove((Object)cloudServer.getPort());
        }else {
            CloudServerManager.getInstance().getUsedSpigotPorts().remove((Object)cloudServer.getPort());
        }
        CloudServerManager.getInstance().getCloudServer().remove(uuid);
        if(process == null)return;
        CloudLogger.getInstance().log("Process found!");
        process.destroy();
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(() -> {
            if(process.isAlive()) {
                CloudLogger.getInstance().log("Process still alive!");
                CloudLogger.getInstance().log("Exiting...");
                process.destroyForcibly();
            }
        }, 3, TimeUnit.SECONDS);
    }

    public ArrayList<CloudServer> getServersByTemplate(String serverGroup) {
        ArrayList<CloudServer> list = new ArrayList<>();
        for (CloudServer server : cloudServer.values()) {
            if (server.getTemplate().getName().equalsIgnoreCase(serverGroup)) {
                list.add(server);
            }
        }
        return list;
    }

    private ArrayList<Integer> usedSpigotPorts = new ArrayList<>();

    private ArrayList<Integer> usedProxyPorts = new ArrayList<>();

    public ArrayList<Integer> getUsedProxyPorts() {
        return usedProxyPorts;
    }

    public ArrayList<Integer> getUsedSpigotPorts() {
        return usedSpigotPorts;
    }

    public HashMap<UUID, CloudServer> getCloudServer() {
        return cloudServer;
    }

    private static CloudServerManager instance;

    public static CloudServerManager getInstance() {
        return instance;
    }

    public CloudServer getServerByName(String name) {
        for (CloudServer server : getCloudServer().values()) {
            if (server.getName().equalsIgnoreCase(name)) return server;
        }
        return null;
    }

    public CloudServer getServerByUUID(UUID uuid) {
        return getCloudServer().get(uuid);
    }

    private SpigotServerManager spigotServerManager;
    private ProxyServerManager proxyServerManager;

    public CloudServerManager() {
        instance = this;
        this.proxyServerManager = new ProxyServerManager();
        this.spigotServerManager = new SpigotServerManager();
    }

    public void updateServerInfo(UUID serverUUID, CloudServerInfo serverInfo) {
        if (cloudServer.get(serverUUID) == null) {
            CloudLogger.getInstance().log("[Error] Tried to update CloudServerInfo for " + serverUUID + ", but this server does not exist!");
            return;
        }
        getCloudServer().get(serverUUID).updateCloudServerInfo(serverInfo);
    }

    public int getUsedRam(String slave) {
        int ram = 0;
        for (CloudServer server : getCloudServer().values()) {
            if (server.getTemplate().getSlave().equals(slave)) {
                ram+= server.getRam();
            }
        }
        return ram;
    }

    public int getUsedRam() {
        int ram = 0;
        for (CloudServer all : getCloudServer().values()) {
            ram += all.getRam();
        }
        return ram;
    }

    public synchronized CloudServer prepareAndStartServer(ServerTemplate template, ServerTemplate copyTemplate, UUID serverUUID) {
        int usedRam = getUsedRam();
        if(usedRam >= CloudSlaveManager.getInstance().getThisSlave().getMaxRam()) {
            CloudLogger.getInstance().log("[Error] Could not start a new server for " + template.getName() + " because no more RAM is available for this Slave.");
            return null;
        }
        if (template.getServerVersion() == ServerVersion.PROXY) {
            CloudServer cloudServer = getProxyServerManager().createNewServerFromTemplate(template, serverUUID);
            getCloudServer().put(cloudServer.getServerUUID(), cloudServer);
            getProxyServerManager().cloneTemplate(cloudServer);
            getProxyServerManager().startServer(cloudServer);
            return cloudServer;
        }
        CloudServer cloudServer = getSpigotServerManager().createNewServerFromTemplate(template, copyTemplate, serverUUID);
        getCloudServer().put(cloudServer.getServerUUID(), cloudServer);
        getSpigotServerManager().cloneTemplate(cloudServer);
        getSpigotServerManager().startServer(cloudServer);
        return cloudServer;
    }


    public ProxyServerManager getProxyServerManager() {
        return proxyServerManager;
    }

    public SpigotServerManager getSpigotServerManager() {
        return spigotServerManager;
    }

    public void unregisterServer(UUID serverUUID) {
        CloudServer cloudServer = getCloudServer().get(serverUUID);
        if (cloudServer == null) return;
        if (cloudServer.getTemplate().getServerVersion() == ServerVersion.SPIGOT) {
            getUsedSpigotPorts().remove((Object)cloudServer.getPort());
        } else {
            getUsedProxyPorts().remove((Object)cloudServer.getPort());
        }
        getCloudServer().remove(serverUUID);
    }
}
