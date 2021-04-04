package dev.javaprojekt.cloudsystem.socket;


import dev.javaprojekt.cloudsystem.cloud.cloudpacket.CloudPacketHandler;
import dev.javaprojekt.cloudsystem.cloud.server.ServerGroup;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class SocketServerHandler extends SimpleChannelInboundHandler<Object> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) {
        (new Thread(() -> {
            if (object == null) {
                CloudLogger.getInstance().log("[Error] An invalid packet was sent: null");
                return;
            }
            try {
                CloudPacketHandler.handlePacket(object);
                ReferenceCountUtil.release(object);
            } finally {
                ReferenceCountUtil.release(object);
            }
        })).start();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    }
}