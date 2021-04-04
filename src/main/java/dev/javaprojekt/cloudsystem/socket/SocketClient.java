package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.server.ServerGroup;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class SocketClient {
    private final String host;
    private final int port;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private ChannelFuture channelFuture;

    private Channel channel;

    public ChannelFuture getChannelFuture() {
        return this.channelFuture;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void start() {
        System.out.println("Connecting to cloud...");
        new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                if (scanner.nextLine().equalsIgnoreCase("send")) {
                    System.out.println("Sending packet...");
                    ServerGroup group = new ServerGroup("Test", ServerType.TEMPLATE, ServerVersion.SPIGOT, 512, 1, 1);
                    channel.writeAndFlush(group);
                }
            }
        }).start();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new ObjectDecoder(2147483647, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                        socketChannel.pipeline().addLast((ChannelHandler)new ObjectEncoder());
                        socketChannel.pipeline().addLast((ChannelHandler)new SocketClientHandler());
                    }
                });
                this.channelFuture = bootstrap.connect(this.host, this.port);
                this.channel = this.channelFuture.channel();
                ChannelFuture closeFuture = this.channel.closeFuture();
                closeFuture.addListener(channelFuture1 -> {
                    Thread curr = Thread.currentThread();
                    while (!curr.isInterrupted());
                });
                closeFuture.sync();
            } catch (Exception exc) {
                exc.printStackTrace();
                System.out.println("[1] Could not connect to CloudSystem! Reconnect in 2 seconds...");
                System.out.println("Netty thread stopped.");
            }
        } finally {
            System.out.println("Netty thread stopped.");
        }
    }

    public static void main(String[] args) throws Exception {
       /* if(args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();

        */
        final String host = "localhost";
        final int port = 7000;
        new SocketClient(host, port).start();
    }
}