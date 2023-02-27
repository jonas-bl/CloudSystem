package dev.javaprojekt.cloudsystem.cloud.config.cinfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.javaprojekt.cloudsystem.cloud.config.cf.CloudConfig;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class ServerInfoFileManager {

    private static ServerInfoFile cloudConfig;

    public static void create(String commanderAddress, CloudServer cloudServer, File file) {
            try {
                file.createNewFile();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gson.toJson(new ServerInfoFile(commanderAddress, cloudServer.getServerUUID(), cloudServer.getName()));
                FileManager.writeToFile(file, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void init() {
        File file = new File("serverinfo.cinfo");
        try {
            cloudConfig = new Gson().fromJson(new FileReader(file), ServerInfoFile.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ServerInfoFile getCloudConfig() {
        return cloudConfig;
    }
}
