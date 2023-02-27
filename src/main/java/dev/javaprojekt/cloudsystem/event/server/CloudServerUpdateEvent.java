package dev.javaprojekt.cloudsystem.event.server;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServerObject;
import dev.javaprojekt.cloudsystem.event.CloudEvent;
import dev.javaprojekt.cloudsystem.server.api.CloudAPI;

public class CloudServerUpdateEvent implements CloudEvent {

    private String serverName;
    private CloudServerUpdateType updateType;

    public CloudServerUpdateEvent(String serverName, CloudServerUpdateType updateType) {
        this.serverName = serverName;
        this.updateType = updateType;
    }

    public String getServerName() {
        return serverName;
    }

    public CloudServerUpdateType getUpdateType() {
        return updateType;
    }

    public CloudServerObject getServer() {
       return CloudAPI.getInstance().getServer(getServerName());
    }
}
