package me.rina.rocan.client.module.misc;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.api.util.chat.ChatSuffixUtil;
import me.rina.rocan.api.util.client.FlagUtil;
import me.rina.rocan.client.event.network.SendEventPacket;
import net.minecraft.network.play.client.CPacketChatMessage;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 04/02/2021 at 00:28
 **/
public class ModuleChatSuffix extends Module {
    public static ValueEnum settingIgnorePrefixes = new ValueEnum("Ignore Prefixes", "IgnorePrefixes", "Ignore specified characters.", FlagUtil.True);

    public static ValueString settingIgnoredPrefixes = new ValueString("Ignored Prefixes", "IgnoredPrefixes", "Characters to ignore.", "/ ! ; & $ ( \\ : . @ * # )");
    public static ValueString settingSuffix = new ValueString("Suffix", "Suffix", "The lower case suffix.", "rocan");

    public ModuleChatSuffix() {
        super("Chat Suffix", "ChatSuffix", "Send at end message the custom client suffix.", ModuleCategory.MISC);
    }

    @Override
    public void onSetting() {
        settingIgnoredPrefixes.setEnabled(settingIgnorePrefixes.getValue() == FlagUtil.True);
    }

    @Listener
    public void onListen(SendEventPacket event) {
        if ((event.getPacket() instanceof CPacketChatMessage) == false) {
            return;
        }

        CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
        String message = packet.getMessage();

        // We need verify if has prefix or no.
        boolean isContinuable = true;

        if (settingIgnoredPrefixes.isEnabled()) {
            for (String prefixes : settingIgnoredPrefixes.getValue().split(" ")) {
                if (message.startsWith(prefixes)) {
                    isContinuable = false;
                }
            }
        }

        if (isContinuable == false) {
            packet.message = message;

            return;
        }

        message += " " + ChatSuffixUtil.hephaestus(settingSuffix.getValue());
        packet.message = message;
    }
}
