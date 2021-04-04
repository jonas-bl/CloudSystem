package dev.javaprojekt.cloudsystem.socket;

import dev.javaprojekt.cloudsystem.cloud.server.ServerGroup;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerType;
import dev.javaprojekt.cloudsystem.cloud.server.enums.ServerVersion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class SocketClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connected!");
        ServerGroup group = new ServerGroup("Test", ServerType.TEMPLATE, ServerVersion.SPIGOT, 512, 1, 1);
        ctx.writeAndFlush(group);
        System.out.println("Sent object to channel: " + group);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object object) {
        System.out.println("Client received: " + object);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}