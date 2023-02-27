package dev.javaprojekt.cloudsystem.server.spigot.sign;

import org.bukkit.Location;

public class CloudSign {

    private String id;
    private String template;
    private Location signLocation;
    private String connectedServer = null;


    public CloudSign(String id, String template, Location signLocation) {
        this.template = template;
        this.signLocation = signLocation;
    }

    public String getConnectedServer() {
        return connectedServer;
    }

    public void setConnectedServer(String connectedServer) {
        this.connectedServer = connectedServer;
    }

    public String getId() {
        return id;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public String getTemplate() {
        return template;
    }
}
