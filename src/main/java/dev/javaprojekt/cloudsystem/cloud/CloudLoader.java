package dev.javaprojekt.cloudsystem.cloud;

import dev.javaprojekt.cloudsystem.cloud.commander.CloudCommander;
import dev.javaprojekt.cloudsystem.cloud.config.cf.CloudConfigManager;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommandHandler;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.slave.CloudSlave;
import dev.javaprojekt.cloudsystem.cloud.slave.manager.CloudSlaveManager;
import dev.javaprojekt.cloudsystem.cloud.util.license.LicenseChecker;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;
import dev.javaprojekt.cloudsystem.enums.ModuleType;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.IOException;
import java.util.Scanner;

public class CloudLoader {

    private static ModuleType moduleType;

     /*

    Created on 28th February 2021
    by Jonas Bleisteiner
    All rights belong to the author.
    Any unauthorized use, decompiling or editing is strictly prohibited

     */


    public static void main(String[] args) {
        new CloudLogger(null);
        String module = null;
        for (String arg : args) {
            if (arg.equals("--module=COMMANDER")) {
                module = "COMMANDER";
                break;
            }
            if (arg.equals("--module=SLAVE")) {
                module = "SLAVE";
                break;
            }
        }
        boolean isModuleDefined = false;
        if (module == null || module.isEmpty()) {
            System.out.println("[Cloud] No module selected!");
        } else {
            moduleType = ModuleType.valueOf(module.toUpperCase());
            isModuleDefined = true;
            System.out.println("[Cloud] Selected module " + moduleType.name());
        }
        System.out.println("  _____ _                 _  _____           _                 \n" +
                " / ____| |               | |/ ____|         | |                \n" +
                "| |    | | ___  _   _  __| | (___  _   _ ___| |_ ___ _ __ ___  \n" +
                "| |    | |/ _ \\| | | |/ _` |\\___ \\| | | / __| __/ _ \\ '_ ` _ \\ \n" +
                "| |____| | (_) | |_| | (_| |____) | |_| \\__ \\ ||  __/ | | | | |\n" +
                " \\_____|_|\\___/ \\__,_|\\__,_|_____/ \\__, |___/\\__\\___|_| |_| |_|\n" +
                "                                    __/ |                      \n" +
                "                                   |___/        by Jonas B.   v.1.3.2          \n");
        System.out.println("[Cloud] Initiating cloudsystem...");
        if (!isModuleDefined) {
            System.out.println("[Cloud] No valid module in start script defined!");
            System.out.println("[Cloud] Please select a module to start:");
            System.out.println("[Cloud] Commander | 1");
            System.out.println("[Cloud] Slave | 2");
            CloudLogger.getInstance().printInputLine();
            Scanner scanner = new Scanner(System.in);
            while (!isModuleDefined) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("commander") || input.equalsIgnoreCase("1")) {
                    isModuleDefined = true;
                    moduleType = ModuleType.COMMANDER;
                    System.out.println("Loading module COMMANDER...");
                } else if (input.equalsIgnoreCase("slave") || input.equalsIgnoreCase("2")) {
                    isModuleDefined = true;
                    moduleType = ModuleType.SLAVE;
                    System.out.println("Loading module SLAVE...");
                } else {
                    System.out.println("[Cloud] Invalid module!");
                    System.out.println("[Cloud] Commander | 1");
                    System.out.println("[Cloud] Slave | 2");
                }
            }
        }
        loadBasics();
        if (moduleType == ModuleType.COMMANDER) {
            new ConsoleCommandHandler();
            new CloudCommander().enable();
        }
        if (moduleType == ModuleType.SLAVE) {
            new CloudSlave().enable();
        }
    }

    public static String getPrefix() {
        return "[" + getModuleType().getName() + "] ";
    }

    public static ModuleType getModuleType() {
        return moduleType;
    }

    public static void loadBasics() {
        CloudConfigManager.init();
        FileManager.createFiles();
        new CloudServerManager();
        try {
            new ServerTemplateManager().importTemplates();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloudSlaveManager cloudSlaveManager = new CloudSlaveManager();
        cloudSlaveManager.importSlaves();
    }
}
