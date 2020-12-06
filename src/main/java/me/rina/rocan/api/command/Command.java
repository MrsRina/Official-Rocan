package me.rina.rocan.api.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.util.chat.ChatUtil;

/**
 * @author SrRina
 * @since 16/11/20 at 12:05pm
 */
public class Command {
    private String[] alias;

    private String description;

    public Command(String[] alias, String description) {
        this.alias = alias;
        this.description = description;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public String[] getAlias() {
        return alias;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /*
     * Tools.
     */
    public void splash() {
        ChatUtil.print(ChatFormatting.RED + setSyntax());
    }

    public void splash(String splash) {
        ChatUtil.print(splash);
    }

    /*
     * Overrides.
     */
    public String setSyntax() {
        return null;
    }

    public void onCommand(String[] args) {}
}