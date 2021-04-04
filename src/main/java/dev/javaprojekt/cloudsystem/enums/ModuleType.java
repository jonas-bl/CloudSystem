package dev.javaprojekt.cloudsystem.enums;

import dev.javaprojekt.cloudsystem.cloud.util.string.StringUtils;

public enum ModuleType {

    COMMANDER,
    SLAVE;

    public String getName() {
        return StringUtils.capitalize(this.name().toLowerCase());
    }
}
