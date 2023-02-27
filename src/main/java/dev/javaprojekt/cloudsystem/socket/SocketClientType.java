package dev.javaprojekt.cloudsystem.socket;

public enum SocketClientType {

    SLAVE,
    PROXY,
    SPIGOT;

    private static SocketClientType socketClientType;

    public static SocketClientType getSocketClientType() {
        return socketClientType;
    }

    public static void setSocketClientType(SocketClientType socketClientType) {
        SocketClientType.socketClientType = socketClientType;
    }
}
