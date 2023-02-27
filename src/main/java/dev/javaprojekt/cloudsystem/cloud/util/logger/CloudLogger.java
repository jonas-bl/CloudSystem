package dev.javaprojekt.cloudsystem.cloud.util.logger;

import dev.javaprojekt.cloudsystem.cloud.CloudLoader;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleColor;
import org.jline.reader.LineReader;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CloudLogger {

    private static CloudLogger instance;

    private LineReader lineReader;

    public CloudLogger(LineReader lineReader) {
        instance = this;
        this.lineReader = lineReader;
    }

    public static CloudLogger getInstance() {
        return instance;
    }

    public void printMessage(String text) {
        if (lineReader == null) System.out.println("[" + getDate() + " " + getTime() + "] " + text);
        else
            lineReader.printAbove("[" + getDate() + " " + getTime() + "] " + text);

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
            return ConsoleColor.CYAN.toString() + "admin@cloud » " + ConsoleColor.WHITE.toString();
        }
        return ConsoleColor.CYAN.toString() + CloudLoader.getModuleType().name().toLowerCase() + "@cloud » " + ConsoleColor.WHITE.toString();
    }
}
