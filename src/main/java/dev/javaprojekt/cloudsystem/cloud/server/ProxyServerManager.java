package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ProxyServerManager {

    public CloudServer createNewServerFromTemplate(ServerTemplate serverTemplate, UUID serverUUID) {
        int port = getAvailablePort();
        int id = getAvailableServerId(serverTemplate.getName());
        CloudServerManager.getInstance().getUsedSpigotPorts().add(port);
        CloudServer cloudServer = new CloudServer(serverTemplate, null, id, CloudSlaveManager.getInstance().getSlaveByName(serverTemplate.getSlave()).getAdress(), port, serverTemplate.getRam(), serverUUID, ServerState.OFFLINE, null, null);
        cloudServer.setDefaultCloudServerInfo();
        return cloudServer;
    }

    public Integer getAvailablePort() {
        int port = 25565;
        while (CloudServerManager.getInstance().getUsedSpigotPorts().contains(port)) {
            port++;
        }
        return port;
    }

  /*  public Integer getAvailableServerId(String template) {
        Integer id = 1;
        for (CloudServer server : CloudServerManager.getInstance().getServersByTemplate(template)) {
            if (server.getId() == id) {
                id++;
            }
        }
        return id;
    }

   */

    public Integer getAvailableServerId(String template) {
        Integer id = 1;
        ArrayList<Integer> taken = new ArrayList<>();
        for (CloudServer server : CloudServerManager.getInstance().getServersByTemplate(template)) {
           taken.add(server.getId());
        }
        while(taken.contains(id)) {
            id++;
        }
        return id;
    }

    public synchronized void cloneTemplate(CloudServer server) {
        try {
            if (!server.isStatic()) {
                FileManager.copyFolder(FileManager.getTemplatesDirectory(server.getTemplate().getName()).toPath(), FileManager.getTempDirectory(server.getTempName()).toPath());
                FileManager.copyFolder(FileManager.getTemplatesDirectory("Global/Proxy").toPath(), FileManager.getTempDirectory(server.getTempName()).toPath());

            }
            File dest = server.isStatic() ? new File("static/" + server.getName() + "/plugins/") : FileManager.getTempDirectory(server.getTempName() + "/plugins/");
            if (!dest.exists()) dest.mkdirs();
            FileManager.copyFileUsingStream(new File("CloudSystem.jar"), new File(dest.toString(), "CloudSystem.jar"));
            FileManager.createServerInfoFile(server);
        } catch (IOException e) {
            e.printStackTrace();
            CloudLogger.getInstance().log("Could not clone template " + server.getTemplate() + " for server " + server.getName());
        }
    }

    public void startServer(CloudServer server) {
        File file = server.isStatic() ? new File("static/" + server.getName() + "/bungeecord.jar") : new File("temp/" + server.getTempName() + "/bungeecord.jar");
        if (!file.exists()) {
            CloudLogger.getInstance().log("[Error] Could NOT start server " + server.getName() + " because bungeecord.jar does not exist in temp directory.");
            return;
        }
        //  List<String> parameter = Arrays.asList("/bin/sh", "-c", "screen -mdS " + server.getName(), "java", "-Xmx" + server.getRam() + "M", "-Xms" + server.getRam() + "M", "-Dcom.mojang.eula.agree=true", "-jar", "spigot.jar", "--port", String.valueOf(server.getPort()));
        //  List<String> parameter = Arrays.asList("/bin/sh", "-c", "screen -mdS " + server.getName() + " && java -Xmx" + server.getRam() + "M -Xms" + server.getRam() + "M -Dcom.mojang.eula.agree=true -jar spigot.jar --port " + String.valueOf(server.getPort()));
        // ProcessBuilder processBuilder = new ProcessBuilder(parameter);

        try {
            File parent = server.isStatic() ? new File("static/" + server.getName() + "/") : new File("temp/" + server.getTempName() + "/");
            Process process = (new ProcessBuilder("/bin/sh", "-c", "screen -mdS " + server.getTempName() + " /bin/sh -c 'cd " + parent.getAbsolutePath() + " && java -server -Xmx" + server.getRam() + "M -jar bungeecord.jar'")).start();
            //  processBuilder.directory(parent);
            //  processBuilder.start();
            CloudServerManager.getInstance().getServerProcess().put(server.getServerUUID(), process);
            CloudLogger.getInstance().log("Starting server " + server.getName() + "...");
        } catch (IOException e) {
            e.printStackTrace();
            CloudLogger.getInstance().log("Could not start server " + server.getName() + "!");
        }
    }

}
