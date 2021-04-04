package dev.javaprojekt.cloudsystem.cloud.command;

import dev.javaprojekt.cloudsystem.cloud.commander.CloudCommander;
import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleCommand;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.util.Arrays;

public class HelpCommand extends ConsoleCommand {

    @Override
    public void onCommand(String[] args) {

        String prefix = CloudCommander.getPrefix();
        System.out.println(prefix + "CloudSystem command overview:");
        System.out.println(prefix + "- create <template / static> <name> <spigot / proxy> <ram> <maxonline>");
        System.out.println(prefix + "- startserver <server>");
        System.out.println(prefix + "- stopserver <server>");
        System.out.println(prefix + "- startgroup <group>");
        System.out.println(prefix + "- stopgroup <group>");
        System.out.println(prefix + "- serverinfo <server>");
        System.out.println(prefix + "- groupinfo <group>");
        System.out.println(prefix + "- command <server> <command ...>");
        System.out.println(prefix);
        System.out.println(prefix + "Debug commands:");
        System.out.println(prefix + "- stopnetty");
        System.out.println(prefix + "- pingpong <message ...>");
    }
}
