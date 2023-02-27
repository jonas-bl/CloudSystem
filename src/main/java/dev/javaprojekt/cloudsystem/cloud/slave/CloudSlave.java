package dev.javaprojekt.cloudsystem.cloud.slave;

import com.google.gson.Gson;
import dev.javaprojekt.cloudsystem.cloud.CloudLoader;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.request.CloudRequestServerStopPacket;
import dev.javaprojekt.cloudsystem.cloud.cloudpacket.slave.CloudSlaveDisconnectPacket;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveObject;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.file.FileManager;
import dev.javaprojekt.cloudsystem.socket.SocketClient;
import dev.javaprojekt.cloudsystem.socket.SocketClientType;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CloudSlave {

    /*

    Created on 28th february 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing may result in criminal prosecution.

     */

    private String commanderAdress;

    public String getCommanderAdress() {
        return commanderAdress;
    }

    private static SocketClient socketClient;

    private static CloudSlaveObject cloudSlaveObject;

    public static SocketClient getSocketClient() {
        return socketClient;
    }

    private static CloudSlave instance;

    public static CloudSlave getInstance() {
        return instance;
    }

    public void restartSocket() {
        socketClient = new SocketClient(commanderAdress, 24300);
        Thread thread = new Thread(socketClient);
        thread.start();
    }

    public void disable() {
        CloudLogger.getInstance().log("Stopping slave...");
        getSocketClient().getChannel().writeAndFlush(new CloudSlaveDisconnectPacket(getCloudSlaveObject().getName()));
        for (CloudServer server : CloudServerManager.getInstance().getCloudServer().values()) {
            CloudLogger.getInstance().log("Stopping server " + server.getName() + " ...");
            getSocketClient().getChannel().writeAndFlush(new CloudRequestServerStopPacket(server.getServerUUID()));

        }
        getSocketClient().stop();
        CloudLogger.getInstance().log("Closed netty connection.");
    }

    public void enable() {
        instance = this;
        SocketClientType.setSocketClientType(SocketClientType.SLAVE);
        if (new File("config/slaves/commander.json").exists()) {
            try {
                commanderAdress = new Gson().fromJson(new FileReader("config/slaves/commander.json"), String.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        cloudSlaveObject = CloudSlaveManager.getInstance().getThisSlave();
        CloudLogger.getInstance().log("Creating socket server...");
        socketClient = new SocketClient(commanderAdress, 24300);
        Thread thread = new Thread((Runnable) socketClient);
        thread.start();

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
                    CloudLogger.getInstance().log("No commands can be executed in the CloudSlave. Press CTRL + C to stop the Slave.");
                    CloudLogger.getInstance().printInputLine();
                    if (input.equalsIgnoreCase("end")) System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        FileManager.cleanDirectory(FileManager.getTempDirectory());
    }

    public static String getPrefix() {
        return "[" + CloudLoader.getModuleType().getName() + "] ";
    }

    public static CloudSlaveObject getCloudSlaveObject() {
        return cloudSlaveObject;
    }
}
