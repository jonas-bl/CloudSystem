package dev.javaprojekt.cloudsystem.cloud.cloudpacket.message;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.message.enums.CloudNotifyType;

public class CloudNotifyPacket implements ICloudPacket {


    private CloudNotifyType cloudNotifyType;
    private String value;

    public CloudNotifyPacket(CloudNotifyType cloudNotifyType, String value) {
        this.cloudNotifyType = cloudNotifyType;
        this.value = value;
    }

    public CloudNotifyType getCloudNotifyType() {
        return cloudNotifyType;
    }

    public String getValue() {
        return value;
    }
}
