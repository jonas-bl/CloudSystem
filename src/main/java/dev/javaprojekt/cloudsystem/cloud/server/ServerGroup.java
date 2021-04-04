package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;

import java.io.Serializable;
import java.util.HashMap;

public class ServerGroup implements Serializable {

    private static HashMap<String, ServerGroup> groups = new HashMap<>();

    public static HashMap<String, ServerGroup> getGroups() {
        return groups;
    }

    public static ServerGroup getByName(String name) {
        return getGroups().get(name);
    }

    String name;
    int ram;
    int minOnline;
    int maxOnline;
    ServerType serverType;
    ServerVersion serverVersion;

    public ServerGroup(String name, ServerType serverType, ServerVersion serverVersion, int ram, int minOnline, int maxOnline) {
        this.name = name;
        this.ram = ram;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
        this.serverType = serverType;
        this.serverVersion = serverVersion;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public int getRam() {
        return ram;
    }

    public String getName() {
        return name;
    }

    public int getMaxOnline() {
        return maxOnline;
    }

    public int getMinOnline() {
        return minOnline;
    }
}
