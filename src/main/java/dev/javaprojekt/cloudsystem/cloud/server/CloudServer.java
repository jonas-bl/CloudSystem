package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class CloudServer implements Serializable {

    private static final long serialVersionUID = 145672346573L;

    private ServerTemplate template;
    private ServerTemplate copyTemplate;
    private String customName;
    private int id;
    private String adress;
    private int port;
    private int ram;
    private UUID serverUUID;
    private CloudServerInfo cloudServerInfo = null;
    private ServerState serverState;

    private Channel nettyChannel;

    public CloudServer(ServerTemplate template, ServerTemplate copyTemplate, int id, String adress, int port, int ram, UUID serverUUID, ServerState serverState, CloudServerInfo cloudServerInfo, Channel nettyChannel) {
        this.template = template;
        this.copyTemplate = copyTemplate;
        this.id = id;
        this.adress = adress;
        this.port = port;
        this.ram = ram;
        this.serverUUID = serverUUID;
        this.serverState = serverState;
        this.cloudServerInfo = cloudServerInfo;
        this.nettyChannel = nettyChannel;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public ServerTemplate getCopyTemplate() {
        return copyTemplate;
    }

    public CloudServerObject constructObject() {
        return new CloudServerObject(getTemplate(), getId(), getAdress(), getPort(), getRam(), getServerUUID(), getServerState(), getCloudServerInfo());
    }

    public void setDefaultCloudServerInfo() {
        updateCloudServerInfo(new CloudServerInfo(getServerUUID(), getName(), "A Cloud Server", "", new ArrayList<>(), 25, "Lobby"));
    }

    public boolean isStatic() {
        return getTemplate().getServerType() == ServerType.STATIC;
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

    public Channel getNettyChannel() {
        return nettyChannel;
    }

    public void setNettyChannel(Channel channel) {
        this.nettyChannel = channel;
    }

    public void writeToChannel(Object object) {
        getNettyChannel().writeAndFlush(object);
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
        return customName != null ? customName + "-" + getId() : template.getName() + "-" + getId();
    }
}
