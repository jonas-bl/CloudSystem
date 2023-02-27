package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class CloudServerObject implements Serializable {

    private static final long serialVersionUID = 145672346573L;

    private ServerTemplate template;
    private int id;
    private String adress;
    private int port;
    private int ram;
    private UUID serverUUID;
    private CloudServerInfo cloudServerInfo = null;
    private ServerState serverState;

    public CloudServerObject(ServerTemplate template, int id, String adress, int port, int ram, UUID serverUUID, ServerState serverState, CloudServerInfo cloudServerInfo) {
        this.template = template;
        this.id = id;
        this.adress = adress;
        this.port = port;
        this.ram = ram;
        this.serverUUID = serverUUID;
        this.serverState = serverState;
        this.cloudServerInfo = cloudServerInfo;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public CloudServerInfo getCloudServerInfo() {
        return cloudServerInfo;
    }

    public void updateCloudServerInfo(CloudServerInfo cloudServerInfo) {
        this.cloudServerInfo = cloudServerInfo;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public String getTempName() {
        return getName() + "_" + getServerUUID().toString();
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public int getRam() {
        return ram;
    }

    public String getAdress() {
        return adress;
    }

    public ServerTemplate getTemplate() {
        return template;
    }

    public String getName() {
        return template.getName() + "-" + getId();
    }
}
