package dev.javaprojekt.cloudsystem.cloud.commander;

import dev.javaprojekt.cloudsystem.cloud.CloudLoader;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.command.*;
import dev.javaprojekt.cloudsystem.cloud.config.cf.CloudConfigManager;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommandHandler;
import dev.javaprojekt.cloudsystem.cloud.player.CloudPlayerManager;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.server.ServerStarterQueue;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.file.FileManager;
import dev.javaprojekt.cloudsystem.rest.RestApplication;
import dev.javaprojekt.cloudsystem.socket.SocketServer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CloudCommander {

    /*

    Created on 28th february 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing is strictly prohibited

     */

    private static String address;
    private static SocketServer socketServer;
    private static CloudCommander instance;

    public static String getAddress() {
        return address;
    }

    public static SocketServer getSocketServer() {
        return socketServer;
    }

    public static CloudCommander getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return "[" + CloudLoader.getModuleType().getName() + "] ";
    }

    public void disable() {
        CloudLogger.getInstance().log("Stopping cloud...");
        ServerStarterQueue.setStopping(true);
        for (CloudServer server : CloudServerManager.getInstance().getCloudServer().values()) {
            CloudLogger.getInstance().log("Stopping server " + server.getName() + " ...");
            if (server.getNettyChannel() != null)
                server.getNettyChannel().writeAndFlush(new CloudRequestServerStopPacket(server.getServerUUID()));
        }
        socketServer.stop();
        CloudLogger.getInstance().log("Closed netty connection.");
        RestApplication.shutdown();
    }

    public void enable() {
        instance = this;
        address = CloudConfigManager.getCloudConfig().getAddress();
        new Thread(() -> {
            if (!new File("config/slaves/commander.json").exists()) {
                try {
                    CloudSlaveManager.getInstance().createCommander(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    CloudLogger.getInstance().log("ERROR: Could not create Commander because no valid IP Address was found on the machine.");
                }
            }
        }).start();
        CloudLogger.getInstance().log("Creating socket server...");
        socketServer = new SocketServer(24300);
        Thread thread = new Thread(socketServer);
        thread.start();
        ConsoleCommandHandler.getInstance().registerCommand("info", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("help", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("?", new HelpCommand());
        ConsoleCommandHandler.getInstance().registerCommand("queue", new QueueCommand());
        ConsoleCommandHandler.getInstance().registerCommand("end", new EndCommand());
        ConsoleCommandHandler.getInstance().registerCommand("create", new CreateCommand());
        ConsoleCommandHandler.getInstance().registerCommand("list", new ListCommand());
        ConsoleCommandHandler.getInstance().registerCommand("startserver", new StartServerCommand());
        ConsoleCommandHandler.getInstance().registerCommand("command", new CMDCommand());
        ConsoleCommandHandler.getInstance().registerCommand("copyserver", new CopyServerCommand());
        ConsoleCommandHandler.getInstance().registerCommand("serverinfo", new ServerInfoCommand());
        ConsoleCommandHandler.getInstance().registerCommand("stopserver", new StopServerCommand());
        ConsoleCommandHandler.getInstance().registerCommand("players", new PlayersCommand());
        ConsoleCommandHandler.getInstance().registerCommand("reload", new ReloadCommand());
        ConsoleCommandHandler.getInstance().registerCommand("clearqueue", new ClearQueueCommand());

        new CloudPlayerManager();
        new ServerStarterQueue().start();

        RestApplication.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::disable));


        CloudLogger.getInstance().printInputLine();
        new Thread(() -> {
            TerminalBuilder terminalBuilder = TerminalBuilder.builder();
            try {
                Terminal terminal = terminalBuilder.dumb(true).build();
                LineReaderBuilder lineReaderBuilder = LineReaderBuilder.builder();
                lineReaderBuilder.terminal(terminal);
                lineReaderBuilder.history(new DefaultHistory());
                LineReader lineReader = lineReaderBuilder.build();
                new CloudLogger(lineReader);
                while (true) {
                    String input = lineReader.readLine();
                    boolean handled = ConsoleCommandHandler.getInstance().handle(input);
                    if (handled) {
                        CloudLogger.getInstance().printInputLine();
                        continue;
                    }
                    System.out.println(getPrefix() + "Unknown command. Type 'help' for a command overview.");
                    CloudLogger.getInstance().printInputLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        FileManager.cleanDirectory(FileManager.getTempDirectory());
    }
}
