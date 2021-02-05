package me.rina.rocan.api.util.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;

/**
 * @author SrRina
 * @since 15/11/20 at 9:43pm
 */
public class ChatUtil {
    public static void print(String message) {
        if (Rocan.getMinecraft().ingameGUI == null) {
            return;
        }

        Rocan.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(ChatFormatting.GRAY + message));
    }

    public static void malloc(String message) {
        if (Rocan.getMinecraft().ingameGUI == null) {
            return;
        }

        Rocan.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(message);
    }

    public static void message(String message) {
        if (Rocan.getMinecraft().player == null) {
            return;
        }

        Rocan.getMinecraft().player.connection.sendPacket(new CPacketChatMessage(message));
    }
}
