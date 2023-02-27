package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.slave.CloudSlave;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.server.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClient implements Runnable {
    private final String host;
    private final int port;
    private UUID serverUUID;
    private int failedConnections = 1;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private ChannelFuture channelFuture;

    private NioEventLoopGroup nioEventLoopGroup;

    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    private Channel channel;

    private boolean ready;

    public void setFailedConnections(int failedConnections) {
        this.failedConnections = failedConnections;
    }

    public boolean isReady() {
        return ready;
    }

    public void setServerUUID(UUID serverUUID) {
        this.serverUUID = serverUUID;
    }

    public UUID getServerUUID() {
        return serverUUID;
    }

    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @SneakyThrows
    public void run() {
        if (SocketClientType.getSocketClientType() == SocketClientType.SLAVE) {
            CloudLogger.getInstance().log("Connecting to the cloud...");
        } else {
            System.out.println("Connecting to the cloud...");
        }
        nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new ObjectDecoder(2147483647, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                    socketChannel.pipeline().addLast(new ObjectEncoder());
                    socketChannel.pipeline().addLast(new SocketClientHandler());
                }
            });
            this.channelFuture = bootstrap.connect(this.host, this.port);
            this.channel = this.channelFuture.channel();
            ChannelFuture closeFuture = this.channel.closeFuture();
            closeFuture.addListener(channelFuture1 -> {
                Thread curr = Thread.currentThread();
                while (!curr.isInterrupted()) ;
            });
            closeFuture.sync();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (SocketClientType.getSocketClientType() == SocketClientType.SLAVE) {
                int i = failedConnections + 1;
                if (i >= 5) {
                    System.exit(0);
                }
                CloudLogger.getInstance().log("Could not connect to the CloudCommander! Reconnecting... (Attempt " + failedConnections + ")");
                final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(() -> {
                    CloudLogger.getInstance().log("Trying to connect to the Commander...");
                    CloudSlave.getInstance().restartSocket();
                    CloudSlave.getSocketClient().setFailedConnections(i);
                }, 6, TimeUnit.SECONDS);
            }else if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
                ProxyServer.getInstance().getConsole().sendMessage(Constants.PREFIX + "§cCould not connect to the Commander!");
                ProxyServer.getInstance().getConsole().sendMessage(Constants.PREFIX + "§cShutting down Proxy...");
                ProxyServer.getInstance().stop();
            }
            else if (SocketClientType.getSocketClientType() == SocketClientType.SPIGOT) {
                Bukkit.getConsoleSender().sendMessage(Constants.PREFIX + "§cCould not connect to the Commander!");
                Bukkit.getConsoleSender().sendMessage(Constants.PREFIX + "§cShutting down Server...");
                Bukkit.shutdown();
            }else {
                System.out.println("Could not connect to the cloud!");
            }
        }
    }

    public void stop() {
        getChannel().close();
        getNioEventLoopGroup().shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        final String host = "91.218.67.143";
        final int port = 24300;
        SocketClient client = new SocketClient(host, port);
        new Thread(client).start();

    }
}