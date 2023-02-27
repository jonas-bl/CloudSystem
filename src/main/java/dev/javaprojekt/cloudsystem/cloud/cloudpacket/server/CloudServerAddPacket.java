package dev.javaprojekt.cloudsystem.cloud.cloudpacket.server;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;

import java.io.Serializable;

public class CloudServerAddPacket implements Serializable {

    private String serverName;
    private String adress;
    private int port;

    public CloudServerAddPacket(String serverName, String adress, int port) {
        this.serverName = serverName;
        this.adress = adress;
        this.port = port;

    }

    public int getPort() {
        return port;
    }

    public String getAdress() {
        return adress;
    }

    public String getServerName() {
        return serverName;
    }
}
