package dev.javaprojekt.cloudsystem.cloud.cloudpacket.template;

import dev.javaprojekt.cloudsystem.cloud.cloudpacket.ICloudPacket;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplate;

public class CloudTemplateUpdatePacket implements ICloudPacket {

    private ServerTemplate template;

    public CloudTemplateUpdatePacket(ServerTemplate template) {
        this.template =template;
    }

    public ServerTemplate getTemplate() {
        return template;
    }
}
