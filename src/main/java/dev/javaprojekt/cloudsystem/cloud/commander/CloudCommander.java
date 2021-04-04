package dev.javaprojekt.cloudsystem.cloud.commander;

import dev.javaprojekt.cloudsystem.cloud.CloudLoader;
import dev.javaprojekt.cloudsystem.cloud.command.EndCommand;
import dev.javaprojekt.cloudsystem.cloud.command.HelpCommand;
import dev.javaprojekt.cloudsystem.cloud.command.StopNettyCommand;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommandHandler;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.socket.SocketServer;

import java.util.Scanner;

public class CloudCommander {

    /*

    Created on 28th february 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing is strictly prohibited and may result in criminal prosecution.

     */

    private static SocketServer socketServer;

    public static SocketServer getSocketServer() {
        return socketServer;
    }

    private static CloudCommander instance;

    public static CloudCommander getInstance() {
        return instance;
    }

    public void disable() {
        CloudLogger.getInstance().log("Stopping cloud...");
        socketServer.stop();
        CloudLogger.getInstance().log("Closed netty connection.");
    }

    public void enable() {
        instance = this;
        CloudLogger.getInstance().log("Creating socket server...");
        socketServer = new SocketServer(6000);
        new Thread(() -> {
            socketServer.start();
        });
        ConsoleCommandHandler.getInstance().registerCommand("info", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("help", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("?", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("stopnetty", new StopNettyCommand());
        ConsoleCommandHandler.getInstance().registerCommand("end", new EndCommand());

        Runtime.getRuntime().addShutdownHook(new Thread(this::disable));

        CloudLogger.getInstance().printInputLine();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            boolean handled = ConsoleCommandHandler.getInstance().handle(input);
            if (handled) {
                CloudLogger.getInstance().printInputLine();
                continue;
            }
            System.out.println(getPrefix() + "Unknown command. Type 'help' for a command overview.");
            CloudLogger.getInstance().printInputLine();
        }
    }

    public static String getPrefix() {
        return "[" + CloudLoader.getModuleType().getName() + "] ";
    }
}
