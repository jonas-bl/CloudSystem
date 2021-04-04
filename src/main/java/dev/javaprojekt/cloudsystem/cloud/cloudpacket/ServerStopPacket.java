package dev.javaprojekt.cloudsystem.cloud.cloudpacket;

import java.io.Serializable;
import java.util.UUID;

public class ServerStopPacket implements Serializable {


    UUID serverUUID;

    public ServerStopPacket(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }
}
