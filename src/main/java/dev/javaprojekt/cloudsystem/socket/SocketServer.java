package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.server.ServerGroup;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class SocketServer {

    NioEventLoopGroup nioEventLoopGroup;

    private Channel channel;

    public int getPort() {
        return port;
    }

    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    private final int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        /*if(args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }

         */
        // new EchoServer(Integer.parseInt(args[0])).start();
        new SocketServer(7000).start();
    }

    public void stop() {
        try {
            getNioEventLoopGroup().shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
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
        System.out.println("Starting socket server...");
        nioEventLoopGroup = new NioEventLoopGroup();
        //NioEventLoopGroup nioEventLoopGroup2 = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(nioEventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new ObjectDecoder(2147483647, ClassResolvers.cacheDisabled(getClass().getClassLoader())));
                    socketChannel.pipeline().addLast(new ObjectEncoder());
                    socketChannel.pipeline().addLast(new SocketServerHandler());
                }
            }).option(ChannelOption.SO_BACKLOG, 128);
            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            this.channel = channelFuture.channel();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
    }
}