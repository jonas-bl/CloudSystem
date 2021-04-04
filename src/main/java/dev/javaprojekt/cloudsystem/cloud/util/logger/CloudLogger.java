package dev.javaprojekt.cloudsystem.cloud.util.logger;

import dev.javaprojekt.cloudsystem.cloud.CloudLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CloudLogger {

    private static CloudLogger instance;

    public CloudLogger() {
        instance = this;
    }

    public static CloudLogger getInstance() {
        return instance;
    }

    public void printMessage(String text) {
        System.out.println("[" + getDate() + " " + getTime() + "] " + text);
    }

    public void log(String text) {
        printMessage(text);
    }

    public void printInputLine() {
        System.out.print(getInputPrefix());
    }

    private String getDate() {
        return (new SimpleDateFormat("dd.MM.yyyy")).format(Calendar.getInstance().getTime());
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public String getInputPrefix() {
        if (CloudLoader.getModuleType() == null) {
            return "admin@cloud » ";
        }
        return CloudLoader.getModuleType().name().toLowerCase() + "@cloud » ";
    }
}
