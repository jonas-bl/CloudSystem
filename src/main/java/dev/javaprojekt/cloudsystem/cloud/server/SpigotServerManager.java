package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.IOException;

public class SpigotServerManager {

    public void cloneTemplate(CloudServer server) {
        try {
            FileManager.copyFolder(FileManager.getTemplatesDirectory(server.getGroup().getName()).toPath(), FileManager.getTempDirectory(server.getTempName()).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            CloudLogger.getInstance().log("Could not clone template " + server.getGroup() + " for server " + server.getName());
        }
    }

    public void startServer(CloudServer server) {
        CloudLogger.getInstance().log("Creating server " + server.getName() + "...");
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-dmS", server.getName(), "java", "-Xmx" + server.getRam() + "M", "-Xms" + server
                .getRam() + "M", "-Dcom.mojang.eula.agree=true", "-jar", "spigot.jar", "--port", String.valueOf(server.getPort()));

        try {
            processBuilder.directory();
            processBuilder.start();
            CloudLogger.getInstance().log("Starting server " + server.getName() + "...");
        } catch (IOException e) {
            e.printStackTrace();
            CloudLogger.getInstance().log("Could not start server " + server.getName() + "!");
        }
    }

}
