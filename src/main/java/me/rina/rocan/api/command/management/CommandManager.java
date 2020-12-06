package me.rina.rocan.api.command.management;

import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.impl.CommandPrefix;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 16/11/20 at 01:23pm
 */
public class CommandManager {
    public static CommandManager INSTANCE;

    private ArrayList<Command> commandList;

    private CommandPrefix commandPrefix;

    public CommandManager() {
        INSTANCE = this;

        this.commandList = new ArrayList<>();

        this.commandPrefix = new CommandPrefix(".");
    }

    public void registry(Command command) {
        this.commandList.add(command);
    }

    public void setCommandList(ArrayList<Command> commandList) {
        this.commandList = commandList;
    }

    public ArrayList<Command> getCommandList() {
        return commandList;
    }

    public static CommandPrefix getCommandPrefix() {
        return INSTANCE.commandPrefix;
    }

    public static Command get(Class clazz) {
        for (Command commands : INSTANCE.getCommandList()) {
            if (commands.getClass() == clazz) {
                return commands;
            }
        }

        return null;
    }

    public static Command get(String _alias) {
        for (Command commands : INSTANCE.getCommandList()) {
            for (String alias : commands.getAlias()) {
                if (alias.equalsIgnoreCase(_alias)) {
                    return commands;
                }
            }
        }

        return null;
    }

    /*
     * Tools.
     */
    public String[] split(String message) {
        return message.replaceFirst(this.commandPrefix.getPrefix(), "").split(" ");
    }
}
