package dev.javaprojekt.cloudsystem.cloud.cloudpacket.respond;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;

import java.util.ArrayList;
import java.util.UUID;

public class CloudRespondTemplatesPacket implements ICloudPacket {

    private UUID requestUUID;
    private ArrayList<ServerTemplate> templates;

    public CloudRespondTemplatesPacket(UUID requestUUID, ArrayList<ServerTemplate> templates) {
        this.requestUUID = requestUUID;
        this.templates = templates;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public ArrayList<ServerTemplate> getTemplates() {
        return templates;
    }
}
