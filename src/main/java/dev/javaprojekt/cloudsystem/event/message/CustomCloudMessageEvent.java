package dev.javaprojekt.cloudsystem.event.message;

import dev.javaprojekt.cloudsystem.event.CloudEvent;

public class CustomCloudMessageEvent implements CloudEvent {

    private CustomCloudMessage cloudMessage;

    public CustomCloudMessageEvent(CustomCloudMessage cloudMessage) {
        this.cloudMessage = cloudMessage;
    }

    public CustomCloudMessage getCloudMessage() {
        return cloudMessage;
    }
}
