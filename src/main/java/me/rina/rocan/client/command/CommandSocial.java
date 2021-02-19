package me.rina.rocan.client.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.social.Social;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.api.social.type.SocialType;
import me.rina.rocan.client.manager.network.PlayerServerManager;
import me.rina.turok.util.TurokClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 19/02/2021 at 11:16
 **/
public class CommandSocial extends Command {
    public CommandSocial() {
        super(new String[] {"social", "s"}, "Social command to add or remove friend/enemy.");
    }

    @Override
    public String setSyntax() {
        return "s/social <list> | <add/new/put/set> <friend/enemy> <name> | <rem/remove/del/delete/unset> <name>";
    }


    @Override
    public void onCommand(String[] args) {
        String first = null;
        String second = null;
        String three = null;

        if (args.length > 1) {
            first = args[1];
        }

        if (args.length > 2) {
            second = args[2];
        }

        if (args.length > 3) {
            three = args[3];
        }

        if (args.length > 4) {
            splash();

            return;
        }

        if (verify(first, "add", "put", "set", "new")) {
            if (verify(second, "friend", "enemy")) {
                if (three == null) {
                    splash();

                    return;
                }

                NetworkPlayerInfo player = PlayerServerManager.get(three);

                if (player == null) {
                    splash("Is the player online?");

                    return;
                }

                if (SocialManager.get(player.getGameProfile().getName()) != null) {
                    splash("Player already at social list");

                    return;
                }

                Social user = new Social(player.getGameProfile().getName(), (SocialType) TurokClass.getEnumByName(SocialType.UNKNOWN, second.toUpperCase()));

                // Add on client.
                Rocan.getSocialManager().registry(user);

                splash("Added " + user.getName() + " as " + second.toLowerCase());
            } else {
                splash(ChatFormatting.RED + "friend/enemy");

            }

            return;
        }

        if (verify(first, "rem", "remove", "delete", "del", "unset")) {
            if (second != null) {
                Social social = SocialManager.get(second);

                if (social == null) {
                    splash("Unknown friend or enemy");

                    return;
                }

                splash("You removed " + social.getName());

                Rocan.getSocialManager().unregister(social);
            } else {
                splash(ChatFormatting.RED + "You do not need specify the type");

            }

            return;
        }

        if (verify(first, "list")) {
            if (three == null) {
                StringBuilder stringBuilder = new StringBuilder();

                if (second == null) {
                    if (Rocan.getSocialManager().getSocialList().isEmpty()) {
                        splash("Social list is empty!");

                        return;
                    }

                    splash("Type " + ChatFormatting.GREEN + "friend " + ChatFormatting.RED + "enemy");
                } else {
                    if (verify(second, "friend", "enemy") == false) {
                        splash(ChatFormatting.RED + "friend/enemy");

                        return;
                    }

                    if (Rocan.getSocialManager().getSocialList().isEmpty()) {
                        splash("Social list is empty!");

                        return;
                    }
                }

                for (Social social : Rocan.getSocialManager().getSocialList()) {
                    if (second != null) {
                        if (second.equalsIgnoreCase("friend") && social.getType() == SocialType.ENEMY) {
                            continue;
                        }

                        if (second.equalsIgnoreCase("enemy") && social.getType() == SocialType.FRIEND) {
                            continue;
                        }
                    }

                    String name = "" + (social.getType() == SocialType.FRIEND ? ChatFormatting.GREEN + social.getName() : ChatFormatting.RED + social.getName()) + ChatFormatting.WHITE;

                    stringBuilder.append(name + "; ");
                }

                if (stringBuilder.length() == 0) {
                    splash("Social list is empty!");
                } else {
                    splash(stringBuilder.toString());
                }
            } else {
                splash();
            }

            return;
        }

        splash();
    }
}
