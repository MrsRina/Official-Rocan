package me.rina.rocan.client.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.util.chat.ChatUtil;

/**
 * @author SrRina
 * @since 16/11/20 at 10:43pm
 */
public class CommandToggle extends Command {
    public CommandToggle() {
        super(new String[] {"t", "toggle"}, "Toggle modules.");
    }

    @Override
    public String setSyntax() {
        return "t/toggle <module>";
    }

    @Override
    public void onCommand(String[] args) {
        String tag = null;

        if (args.length > 1) {
            tag = args[1];
        }

        if (args.length > 2 || tag == null) {
            splash();

            return;
        }

        Module module = ModuleManager.get(tag);

        if (module == null) {
            ChatUtil.print(ChatFormatting.RED + "Unknown module.");

            return;
        }

        module.toggle();

        ValueBoolean toggleMessage = (ValueBoolean) module.get("ToggleMessage");

        if (toggleMessage.getValue() == false) {
            ChatUtil.print("Module has been update to " + Boolean.toString(module.isEnabled()) + ".");
        }
    }
}
