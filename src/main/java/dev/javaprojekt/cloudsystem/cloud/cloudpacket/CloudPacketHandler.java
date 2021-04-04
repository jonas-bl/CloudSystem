package dev.javaprojekt.cloudsystem.cloud.cloudpacket;

import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

public class CloudPacketHandler {

    public static void handlePacket(Object income) {
        if (income.getClass() == ServerStartedPacket.class) {

            return;
        }

        CloudLogger.getInstance().log("[Error] An invalid packet (or object) was sent to the cloud: " + income.getClass());
    }
}
