package me.rina.rocan.client.module.misc;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.setting.value.ValueString;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.FlagUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.turok.util.TurokMath;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 04/02/2021 at 20:28
 **/
public class ModuleSpammer extends Module {
    public static ValueNumber settingDelay = new ValueNumber("Delay", "Delay", "Seconds delay to send message.", 1.5f, 0.5f, 10.0f);
    public static ValueNumber settingLimit = new ValueNumber("Limit", "Limit", "The limit of messages in queue.", 3, 1, 16);

    public static ValueEnum settingWalk = new ValueEnum("Walk", "Walk", "Spam blocks walked.", FlagUtil.True);
    public static ValueString settingWalkText = new ValueString("Walk Text", "WalkText", "The custom text for spam.", "I just walked <blocks>, thanks for Rocan!");

    private double[] lastWalkingPlayerPos;

    public ModuleSpammer() {
        super("Spammer", "Spammer", "Spammer chat.", ModuleCategory.Misc);
    }

    @Override
    public void onSetting() {
        Rocan.getSpammerManager().setDelay(settingDelay.getValue().floatValue());
        Rocan.getSpammerManager().setLimit(settingLimit.getValue().intValue());

        settingWalkText.setEnabled(settingWalk.getValue() == FlagUtil.True);
    }

    @Listener
    public void onListen(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (settingWalkText.isEnabled()) {
            this.verifyWalking();
        }
    }

    public void verifyWalking() {
        if (this.lastWalkingPlayerPos == null) {
            this.lastWalkingPlayerPos = PlayerUtil.getLastPos();
        }

        // If speed is > 0, is because we are moving.
        if (mc.player.movementInput.moveForward > 0f || mc.player.movementInput.moveStrafe > 0f) {
            int x = (int) (this.lastWalkingPlayerPos[0] - PlayerUtil.getPos()[0]);
            int y = (int) (this.lastWalkingPlayerPos[1] - PlayerUtil.getPos()[1]);
            int z = (int) (this.lastWalkingPlayerPos[2] - PlayerUtil.getPos()[2]);

            // x^2 + y^2 + z^2;
            int walkedBlocks = TurokMath.sqrt(x * x + y * y + z * z);

            if (walkedBlocks != 0) {
                Rocan.getSpammerManager().send(settingWalkText.getValue().replaceAll("<blocks>", "" + walkedBlocks));
            }
        } else {
            this.lastWalkingPlayerPos = PlayerUtil.getLastPos();
        }
    }
}
