package dev.javaprojekt.cloudsystem.cloud.util.future;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestPacket;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.server.spigot.CloudSpigot;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncFutureRequest {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static HashMap<UUID, Object> requestResponds = new HashMap<>();

    public static HashMap<UUID, Object> getRequestResponds() {
        return requestResponds;
    }

    public Future<Object> fetch(CloudRequestPacket requestPacket) {
        //  System.out.println("[CloudSystem] AsyncFutureRequest: " + requestResponds.size() + " pending requests.");
        //   System.out.println("Requesting " + requestPacket.getRequestType());
        //  if(requestResponds.size() >= 10){
        // System.out.println("[CloudSystem] Notice: More than 10 pending requests ("+requestResponds.size()+") -> Switching to sync method.");
        return performSync(requestPacket);
     /*   }
        CompletableFuture<Object> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
                CloudBungee.getSocketClient().getChannel().writeAndFlush(requestPacket);
            } else {
                CloudSpigot.getSocketClient().getChannel().writeAndFlush(requestPacket);
            }
            while (!requestResponds.containsKey(requestPacket.getRequestUUID())) ;
            future.complete(requestResponds.get(requestPacket.getRequestUUID()));
            requestResponds.remove(requestPacket.getRequestUUID());
        });
        return future;

      */
    }

    @SneakyThrows
    public Future<Object> performSync(CloudRequestPacket requestPacket) {
        CompletableFuture<Object> future = new CompletableFuture<>();
     //   executorService.submit(() -> {
            if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
                CloudBungee.getSocketClient().getChannel().writeAndFlush(requestPacket);
            } else {
                CloudSpigot.getSocketClient().getChannel().writeAndFlush(requestPacket);
            }
            while (!requestResponds.containsKey(requestPacket.getRequestUUID())) ;
            future.complete(requestResponds.get(requestPacket.getRequestUUID()));
            requestResponds.remove(requestPacket.getRequestUUID());
     //   });
        return future;
    }
}
