package dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;

public class CloudSlaveConnectPacket implements ICloudPacket {

    String slaveName;

    public CloudSlaveConnectPacket(String slaveName) {
        this.slaveName = slaveName;
    }

    public String getSlaveName() {
        return slaveName;
    }
}
