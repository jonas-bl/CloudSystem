package dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

public class CloudSlaveDisconnectPacket implements ICloudPacket {

    String slaveName;

    public CloudSlaveDisconnectPacket(String slaveName) {
        this.slaveName = slaveName;
    }

    public String getSlaveName() {
        return slaveName;
    }
}
