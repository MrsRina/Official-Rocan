package me.rina.rocan.client.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.util.chat.ChatUtil;

/**
 * @author SrRina
 * @since 16/11/20 at 02:03pm
 */
public class CommandPrefix extends Command {
    public CommandPrefix() {
        super(new String[] {"prefix", "p"}, "Set new prefix to chat command.");
    }

    @Override
    public String setSyntax() {
        return "prefix/p <char>";
    }

    @Override
    public void onCommand(String[] args) {
        String _char = null;

        if (args.length > 1) {
            _char = args[1];
        }

        if (args.length > 2 || _char == null) {
            splash();

            return;
        }

        CommandManager.getCommandPrefix().setPrefix(_char);

        this.print("Chat prefix has been update to " + "'" + CommandManager.getCommandPrefix().getPrefix() + "'");
    }
}
