package dev.javaprojekt.cloudsystem.cloud.config.cf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.javaprojekt.cloudsystem.cloud.util.license.LicenseChecker;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CloudConfigManager {

    private static CloudConfig cloudConfig;

    public static void init() {
        LicenseChecker licenseChecker = new LicenseChecker();
        boolean b = licenseChecker.initCheck();
        File file = new File("config/config.json");
        if (!file.exists()) {
            System.out.println("Creating file");
            try {
                file.createNewFile();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gson.toJson(new CloudConfig("127.0.0.1"));
                FileManager.writeToFile(file, jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            cloudConfig = new Gson().fromJson(new FileReader(file), CloudConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static CloudConfig getCloudConfig() {
        return cloudConfig;
    }
}
