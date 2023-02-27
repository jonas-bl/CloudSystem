package dev.javaprojekt.cloudsystem.socket;


import dev.javaprojekt.cloudsystem.cloud.cloudpacket.handler.CommanderPacketHandler;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

public class SocketServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String host = ctx.channel().remoteAddress().toString().replace("/", "").split(":")[0];
        if (!(host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1"))) {
            CloudSlaveObject slaveObject = CloudSlaveManager.getInstance().getSlaveByIp(host);
            if (slaveObject == null) {
                CloudLogger.getInstance().log("WARNING: An invalid IP Address tried to connect to the SocketServer. (" + host + ")");
                ctx.writeAndFlush("Your connection is invalid. You have been disconnected!")
                        .addListener(ChannelFutureListener.CLOSE);
                ctx.channel().disconnect();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) {
       // (new Thread(() -> {
            if (object == null) {
                CloudLogger.getInstance().log("[Error] An invalid packet was sent: null");
                return;
            }
            try {
                CommanderPacketHandler.handlePacket(channelHandlerContext.channel(), object);
                ReferenceCountUtil.release(object);
            } finally {
                ReferenceCountUtil.release(object);
            }
     //   })).start();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        CloudLogger.getInstance().log("A netty exception occured!");
        CloudLogger.getInstance().log("Error: " + cause.getLocalizedMessage());
    }
}