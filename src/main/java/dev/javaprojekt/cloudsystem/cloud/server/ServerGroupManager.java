package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import dev.javaprojekt.cloudsystem.file.FileManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ServerGroupManager {

    private static ServerGroupManager instance;

    public ArrayList<ServerGroup> getGroups() {
        return groups;
    }

    public static ServerGroupManager getInstance() {
        return instance;
    }

    public ServerGroupManager() {
        instance = this;
    }

    public void importGroups() throws IOException, ParseException {
        getGroups().clear();
        File file = new File(FileManager.getConfigDirectory().toString() + "/servergroups/");
        JSONParser parser = new JSONParser();
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(listFile));
            getGroups().add(new ServerGroup(
                    String.valueOf(jsonObject.get("Name")),
                    ServerType.valueOf(String.valueOf(jsonObject.get("ServerType"))),
                    ServerVersion.valueOf(String.valueOf(jsonObject.get("ServerVersion"))),
                    Integer.parseInt(String.valueOf(jsonObject.get("Ram"))),
                    Integer.parseInt(String.valueOf(jsonObject.get("MinOnline"))),
                    Integer.parseInt(String.valueOf(jsonObject.get("MaxOnline")))));
        }
    }

    private ArrayList<ServerGroup> groups = new ArrayList<>();

    public void createGroup(ServerGroup serverGroup) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Name", serverGroup.getName());
        jsonObject.put("ServerType", serverGroup.getServerType());
        jsonObject.put("ServerVersion", serverGroup.getServerVersion());
        jsonObject.put("Ram", serverGroup.getRam());
        jsonObject.put("MinOnline", serverGroup.getMinOnline());
        jsonObject.put("MaxOnline", serverGroup.getMaxOnline());
        FileManager.writeToFile(new File(FileManager.getConfigDirectory().toString() + "/servergroups/" + serverGroup.getName() + ".json"), jsonObject);
    }

}
