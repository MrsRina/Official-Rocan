package me.rina.rocan.client.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.util.chat.ChatUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author SrRina
 * @since 16/11/20 at 02:03pm
 */
public class CommandCoords extends Command {
  public CommandCoords() {
    super(new String[] {"coords", "c"}, "Copies your coordinates to the clipboard");
  }

  @Override
  public String setSyntax() {
    return "coords/c";
  }

  @Override
  public void onCommand(String[] args) {
    if (args.length > 1) {
      splash();

      return;
    }

    String coords = "X: " + Rocan.MC.player.getPosition().getX() + " Y: " + Rocan.MC.player.getPosition().getY() + " Z: " + Rocan.MC.player.getPosition().getZ();
    StringSelection stringSelection = new StringSelection(coords);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
    this.print("Coordinates copied to clipboard");
  }
}
