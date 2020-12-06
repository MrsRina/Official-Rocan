package me.rina.rocan.api.command.impl;

/**
 * @author SrRina
 * @since 16/11/20 at 02:03pm
 */
public class CommandPrefix {
    String prefix;

    public CommandPrefix(String _default) {
        this.prefix =_default;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
