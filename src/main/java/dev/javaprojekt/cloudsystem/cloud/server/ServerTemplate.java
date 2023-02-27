package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;

import java.io.Serializable;
import java.util.HashMap;

public class ServerTemplate implements Serializable {

    private static final long serialVersionUID = 145672346573L;

    String name;
    int ram;
    int minOnline;
    int maxOnline;
    ServerType serverType;
    ServerVersion serverVersion;
    String slave;

    public ServerTemplate(String name, ServerType serverType, ServerVersion serverVersion, int ram, int minOnline, int maxOnline, String slave) {
        this.name = name;
        this.ram = ram;
        this.minOnline = minOnline;
        this.maxOnline = maxOnline;
        this.serverType = serverType;
        this.serverVersion = serverVersion;
        this.slave = slave;
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

    public String getSlave() {
        return slave;
    }
}
