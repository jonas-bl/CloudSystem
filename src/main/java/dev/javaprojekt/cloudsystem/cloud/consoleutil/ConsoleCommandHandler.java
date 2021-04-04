package dev.javaprojekt.cloudsystem.cloud.consoleutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConsoleCommandHandler {

    private static ConsoleCommandHandler instance;

    public static ConsoleCommandHandler getInstance() {
        return instance;
    }

    private static HashMap<String, ConsoleCommand> mainCommands = new HashMap<>();

    public ConsoleCommandHandler() {
        instance = this;
    }

    public boolean handle(String input) {
        String[] args = input.split(" ");
        String command = args[0];
        ArrayList<String>allCommands = new ArrayList<>(Arrays.asList(args));
        allCommands.remove(0);
        String[] subcommands = allCommands.toArray(new String[0]);
        if (mainCommands.containsKey(command)) {
            mainCommands.get(command).onCommand(subcommands);
            return true;
        }
        return false;
    }

    public void registerCommand(String command, ConsoleCommand consoleCommand) {
        mainCommands.put(command, consoleCommand);
    }
}
