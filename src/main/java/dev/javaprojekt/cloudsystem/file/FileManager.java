package dev.javaprojekt.cloudsystem.file;

import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileManager {

    public static void createFiles() {
        if(!getConfigDirectory().exists())getConfigDirectory().mkdirs();
        if(!getTemplatesDirectory().exists())getTemplatesDirectory().mkdirs();
        if(!getTempDirectory().exists())getTempDirectory().mkdirs();
    }

    public static void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private static void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void writeToFile(File file, JSONObject jsonObject) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            CloudLogger.getInstance().log("Could not write JSONObject to file " + file.getName());
        }
    }

    public static File getConfigDirectory() {
        return new File("/config");
    }

    public static File getTempDirectory() {
        return new File("/temp");
    }

    public static File getTempDirectory(String server) {
        return new File("/temp/" + server);
    }

    public static File getTemplatesDirectory() {
        return new File("/templates");
    }

    public static File getTemplatesDirectory(String group) {
        return new File("/templates/" + group);
    }
}
