package dev.javaprojekt.cloudsystem.cloud.slave.manager;

import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerState;
import io.netty.channel.Channel;

import java.io.Serializable;

public class CloudSlaveConfigObject {

    private String name;
    private int maxRam;
    private String adress;

    public CloudSlaveConfigObject(String name, int maxRam, String adress) {
        this.name = name;
        this.maxRam = maxRam;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public int getMaxRam() {
        return maxRam;
    }
}
