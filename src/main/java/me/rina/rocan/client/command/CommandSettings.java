package me.rina.rocan.client.command;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.module.management.ModuleManager;

/**
 * @author SrRina
 * @since 17/03/2021 at 23:44
 **/
public class CommandSettings extends Command {
    public CommandSettings() {
        super(new String[] {"settings", "config"}, "Config command.");
    }

    @Override
    public String setSyntax() {
        return "settings/config <char>";
    }

    @Override
    public void onCommand(String[] args) {
        String mode = null;

        if (args.length > 1) {
            mode = args[1];
        }

        if (args.length > 2 || mode == null) {
            splash();

            return;
        }

        if (verify(mode, "save")) {
            // Finish the preset saving all.
            Rocan.getModuleManager().onSave();
            Rocan.getSocialManager().onSave();

            this.print("Settings saved");
        }

        if (verify(mode, "load")) {
            // Load all managers file.
            Rocan.getModuleManager().onLoad();
            Rocan.getSocialManager().onLoad();

            // Refresh and reload managers.
            ModuleManager.reload();
            ModuleManager.refresh();

            this.print("Settings loaded");
        }
    }
}
