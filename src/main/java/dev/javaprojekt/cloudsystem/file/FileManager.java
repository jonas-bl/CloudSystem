package dev.javaprojekt.cloudsystem.file;

import com.github.underscore.lodash.Json;
import com.github.underscore.lodash.U;
import dev.javaprojekt.cloudsystem.cloud.config.cinfo.ServerInfoFile;
import dev.javaprojekt.cloudsystem.cloud.config.cinfo.ServerInfoFileManager;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.slave.CloudSlave;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import net.md_5.bungee.api.config.ServerInfo;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Stream;

public class FileManager {

    public static void createFiles() {
        if (!getConfigDirectory().exists())  {
            getConfigDirectory().mkdirs();
            getConfigDirectory("slaves").mkdirs();
            getConfigDirectory("templates").mkdirs();
        }
        if (!getTemplatesDirectory().exists()) {
            getTemplatesDirectory().mkdirs();
            getTemplatesDirectory("Global").mkdirs();
            getTemplatesDirectory("Global/Spigot").mkdirs();
            getTemplatesDirectory("Global/Proxy").mkdirs();
        }
        if (!getTempDirectory().exists()) getTempDirectory().mkdirs();
        if (!getStaticDirectory().exists()) getStaticDirectory().mkdirs();

    }

    public static void copyFolder(Path src, Path dest) throws IOException {
      //  try (Stream<Path> stream = Files.walk(src)) {
      //      stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
      //  }
        FileUtils.copyDirectory(src.toFile(), dest.toFile());
    }

    private static void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void writeToFile(File file, String jsonString) {
        new Thread(() -> {
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.write(jsonString);
            } catch (IOException e) {
                CloudLogger.getInstance().log("Could not write JSONObject to file " + file.getName());
                e.printStackTrace();
            } finally {
                try {
                    assert writer != null;
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void cleanDirectory(File file) {
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createServerInfoFile(CloudServer cloudServer) {
        File file = cloudServer.isStatic() ? new File("static/" + cloudServer.getName() + "/serverinfo.cinfo") : new File(getTempDirectory(cloudServer.getTempName()), "serverinfo.cinfo");
        if (file.exists()) {
            file.delete();
        }
        ServerInfoFileManager.create(CloudSlave.getInstance().getCommanderAdress(), cloudServer, file);
    }

    public static UUID getCurrentServerUUID() {
        return ServerInfoFileManager.getCloudConfig().getServerUUID();
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    public static File getConfigDirectory() {
        return new File("config");
    }

    public static File getConfigDirectory(String type) {
        return new File("config/" + type + "/");
    }

    public static File getTempDirectory() {
        return new File("temp");
    }

    public static File getTempDirectory(String server) {
        return new File("temp/" + server + "/");
    }

    public static File getStaticDirectory(String server) {
        return new File("static/" + server + "/");
    }

    public static File getStaticDirectory() {
        return new File("static");
    }

    public static File getTemplatesDirectory() {
        return new File("templates/");
    }

    public static File getTemplatesDirectory(String group) {
        return new File("templates/" + group + "/");
    }
}
