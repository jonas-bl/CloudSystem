package dev.javaprojekt.cloudsystem.cloud.slave.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.javaprojekt.cloudsystem.cloud.config.cf.CloudConfigManager;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class CloudSlaveManager {

    private static CloudSlaveManager instance;

    public static CloudSlaveManager getInstance() {
        return instance;
    }

    private HashMap<String, CloudSlaveObject> slaves = new HashMap<>();

    public HashMap<String, CloudSlaveObject> getSlaves() {
        return slaves;
    }

    public void createCommander(String ip) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(ip);
        File file = new File(FileManager.getConfigDirectory().toString() + "/slaves/");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileManager.writeToFile(new File(FileManager.getConfigDirectory().toString() + "/slaves/commander.json"), jsonString);
    }

    public void importSlaves() {
        getSlaves().clear();
        File file = new File(FileManager.getConfigDirectory().toString() + "/slaves/");
        if (!file.exists()) {
            file.mkdirs();
            return;
        }
        for (File listFile : file.listFiles()) {
            if (listFile.getName().startsWith("commander")) continue;
            CloudSlaveConfigObject slave = null;
            try {
                slave = new Gson().fromJson(new FileReader(listFile), CloudSlaveConfigObject.class);
                getSlaves().put(slave.getName(), new CloudSlaveObject(slave.getName(), slave.getMaxRam(), null, ServerState.OFFLINE, slave.getAdress()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public CloudSlaveManager() {
        instance = this;
    }

    public void createSlave(CloudSlaveConfigObject slaveObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(slaveObject);
        File file = new File(FileManager.getConfigDirectory().toString() + "/slaves/");
        if (!file.exists()) {
            file.mkdirs();
        }
        FileManager.writeToFile(new File(FileManager.getConfigDirectory().toString() + "/slaves/" + slaveObject.getName() + ".json"), jsonString);
    }

    public CloudSlaveObject getSlaveByName(String slave) {
        return getSlaves().get(slave);
    }

    public CloudSlaveObject getThisSlave() {
        String host = CloudConfigManager.getCloudConfig().getAddress();
        for (CloudSlaveObject cloudSlaveObject : getSlaves().values()) {
            if (cloudSlaveObject.getAdress().equals(host)) return cloudSlaveObject;
        }
        return null;
    }

    public CloudSlaveObject getSlaveByIp(String address) {
        for (CloudSlaveObject cloudSlaveObject : getSlaves().values()) {
            if (cloudSlaveObject.getAdress().equals(address)) return cloudSlaveObject;
        }
        return null;
    }
}
