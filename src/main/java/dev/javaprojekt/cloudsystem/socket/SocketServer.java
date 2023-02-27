package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.commander.CloudCommander;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SocketServer implements Runnable {

    private final int port;
    NioEventLoopGroup nioEventLoopGroup1;
    NioEventLoopGroup nioEventLoopGroup2;
    private Channel channel;

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
        new SocketServer(7000).run();
    }

    public Channel getChannel() {
        return channel;
    }

    public NioEventLoopGroup getNioEventLoopGroup1() {
        return nioEventLoopGroup1;
    }

    public NioEventLoopGroup getNioEventLoopGroup2() {
        return nioEventLoopGroup2;
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        try {
            getNioEventLoopGroup1().shutdownGracefully().sync();
            getNioEventLoopGroup2().shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        CloudLogger.getInstance().log("Starting socket server...");
        nioEventLoopGroup1 = new NioEventLoopGroup();
        nioEventLoopGroup2 = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            (serverBootstrap.group(nioEventLoopGroup1, nioEventLoopGroup2).channel(NioServerSocketChannel.class)).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(new ChannelHandler[]{new ObjectDecoder(2147483647, ClassResolvers.cacheDisabled(getClass().getClassLoader()))});
                    socketChannel.pipeline().addLast(new ChannelHandler[]{new ObjectEncoder()});
                    socketChannel.pipeline().addLast(new ChannelHandler[]{new SocketServerHandler()});
                }
            }).option(ChannelOption.SO_BACKLOG, Integer.valueOf(128));
            ChannelFuture channelFuture = serverBootstrap.bind(CloudCommander.getAddress(), this.port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println(exc.getLocalizedMessage());
        }
    }
}