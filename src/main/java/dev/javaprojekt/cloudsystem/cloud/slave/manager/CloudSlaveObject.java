package dev.javaprojekt.cloudsystem.cloud.slave.manager;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import io.netty.channel.Channel;

import java.io.Serializable;

public class CloudSlaveObject implements Serializable {

    private String name;
    private int maxRam;
    private Channel channel;
    private ServerState serverState;
    private String adress;


    public CloudSlaveObject(String name, int maxRam, Channel channel, ServerState serverState, String adress) {
        this.name = name;
        this.maxRam = maxRam;
        this.channel = channel;
        this.serverState = serverState;
        this.adress = adress;
    }

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
    }

    public ServerState getServerState() {
        return serverState;
    }

    public String getName() {
        return name;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getAdress() {
        return adress;
    }

    public int getMaxRam() {
        return maxRam;
    }
}
