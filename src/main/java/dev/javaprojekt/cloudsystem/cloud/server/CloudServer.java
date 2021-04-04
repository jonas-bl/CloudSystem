package dev.javaprojekt.cloudsystem.cloud.server;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.UUID;

public class CloudServer implements Serializable {

    private ServerGroup group;
    private int id;
    private String adress;
    private int port;
    private int ram;
    private UUID serverUUID;

    private Channel nettyChannel;

    public CloudServer(ServerGroup group, int id, String adress, int port, int ram) {
        this.group = group;
        this.id = id;
        this.adress = adress;
        this.port = port;
        this.ram = ram;
        this.serverUUID = UUID.randomUUID();
    }

    public void setNettyChannel(Channel channel) {
        this.nettyChannel = channel;
    }

    public Channel getNettyChannel() {
        return nettyChannel;
    }

    public void writeToChannel(Object object) {
        getNettyChannel().writeAndFlush(object);
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public String getTempName() {
        return getName() + "_" + getServerUUID();
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

    public ServerGroup getGroup() {
        return group;
    }

    public String getName() {
        return getGroup() + "-" + getId();
    }
}
