package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;

import java.util.UUID;

public class CloudRespondTemplateInfoPacket implements ICloudPacket {

    private UUID requestUUID;
    private ServerTemplate serverTemplate;

    public CloudRespondTemplateInfoPacket(UUID requestUUID, ServerTemplate serverTemplate) {
        this.requestUUID = requestUUID;
        this.serverTemplate = serverTemplate;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public ServerTemplate getServerTemplate() {
        return serverTemplate;
    }


}
