package dev.javaprojekt.cloudsystem.event.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CustomCloudMessage implements Serializable {

    private String channel;

    private Map<String, Object> messageContent;

    public CustomCloudMessage(String channel, String key, Object value) {
        messageContent = new HashMap<>();
        messageContent.put(key, value);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public CustomCloudMessage append(String key, Object value) {
        messageContent.put(key, value);
        return this;
    }

    public Object getValue(String key) {
        return messageContent.get(key);
    }
}
