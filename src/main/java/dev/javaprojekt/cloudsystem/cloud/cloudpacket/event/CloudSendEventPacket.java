package dev.javaprojekt.cloudsystem.cloud.cloudpacket.event;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.event.CloudEvent;

import java.util.UUID;

public class CloudSendEventPacket implements ICloudPacket {
    private UUID sender;

    private CloudEvent event;

    public CloudSendEventPacket(UUID sender, CloudEvent event) {
        this.sender = sender;
        this.event = event;
    }

    public CloudEvent getEvent() {
        return this.event;
    }

    public UUID getSender() {
        return this.sender;
    }
}
