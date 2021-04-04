package dev.javaprojekt.cloudsystem.cloud.cloudpacket;

import java.io.Serializable;
import java.util.UUID;

public class ServerStartedPacket implements Serializable {

    UUID serverUUID;

    public ServerStartedPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
