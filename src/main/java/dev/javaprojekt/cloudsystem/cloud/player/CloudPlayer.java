package dev.javaprojekt.cloudsystem.cloud.player;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

import java.util.UUID;

public class CloudPlayer implements ICloudPacket {

    private UUID uuid;
    private String name;
    private String server;
    private String proxy;

    public CloudPlayer(UUID uuid, String name, String server, String proxy) {
        this.uuid = uuid;
        this.name = name;
        this.server = server;
        this.proxy = proxy;
    }

    public String getName() {
        return name;
    }

    public String getProxy() {
        return proxy;
    }

    public String getServer() {
        return server;
    }

    public UUID getUUID() {
        return uuid;
    }
}
