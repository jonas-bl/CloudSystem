package dev.javaprojekt.cloudsystem.event;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.event.CloudSendEventPacket;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import dev.javaprojekt.cloudsystem.socket.SocketClient;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class CloudEventManager {

    private  ArrayList<CloudListener> registeredListener = new ArrayList<>();

    public  ArrayList<CloudListener> getRegisteredListener() {
        return registeredListener;
    }

    private static CloudEventManager instance;

    public static CloudEventManager getInstance() {
        return instance;
    }

    public CloudEventManager() {
        instance = this;
    }

    public void registerEvents(CloudListener listener) {
        System.out.println("Registered Listener " + listener.getClass().getName());
        getRegisteredListener().add(listener);
    }

    public void unregisterEvents(CloudListener listener) {
        getRegisteredListener().remove(listener);
    }

    public void callEvent(CloudEvent event, boolean toChannel) {
        if(toChannel) {
            if(SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
                CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudSendEventPacket(CloudBungee.getServerUUID(), event));
            } else {
                CloudSpigot.getSocketClient().getChannel().writeAndFlush(new CloudSendEventPacket(CloudSpigot.getServerUUID(), event));
            }
        }
        for (CloudListener listener : getRegisteredListener()) {
            Class<?> c = listener.getClass();
            for (Method method : c.getDeclaredMethods()) {
                try {
                    CloudEventHandler eventHandler = method.getAnnotation(CloudEventHandler.class);
                    if (eventHandler != null) {
                        if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                            {
                                method.invoke(listener, event);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Could not call event: " + event.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }
    }
}
