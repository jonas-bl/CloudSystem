package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler.SlavePacketHandler;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServersAddedPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.server.CloudServerStartedPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave.CloudSlaveConnectPacket;
import dev.javaprojekt.cloudsystem.cloud.slave.CloudSlave;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.server.bungee.CloudBungee;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler.BungeePacketHandler;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler.SpigotPacketHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class SocketClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (SocketClientType.getSocketClientType() == SocketClientType.SLAVE) {
            CloudLogger.getInstance().log("Connected!");
        } else {
            System.out.println("Connected!");
              // ctx.channel().writeAndFlush("Hello");
        }
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudServerStartedPacket(CloudBungee.getServerUUID()));
            CloudBungee.getSocketClient().getChannel().writeAndFlush(new CloudRequestServersAddedPacket());
        }
        if (SocketClientType.getSocketClientType() == SocketClientType.SLAVE) {
            ctx.channel().writeAndFlush(new CloudSlaveConnectPacket(CloudSlave.getCloudSlaveObject().getName()));
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object object) {
        if (SocketClientType.getSocketClientType() == SocketClientType.SPIGOT) {
            SpigotPacketHandler.handlePacket(object);
        }
        if (SocketClientType.getSocketClientType() == SocketClientType.PROXY) {
            BungeePacketHandler.handlePacket(ctx.channel(), object);
        }
        if (SocketClientType.getSocketClientType() == SocketClientType.SLAVE) {
            SlavePacketHandler.handlePacket(object);
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("The connection to the cloud was refused.");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
        System.out.println("The connection to the cloud was refused.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}