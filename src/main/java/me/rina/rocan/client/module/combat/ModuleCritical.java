package me.rina.rocan.client.module.combat;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueEnum;
import me.rina.rocan.api.util.client.KeyUtil;
import me.rina.rocan.api.util.entity.PlayerUtil;
import me.rina.rocan.api.util.network.PacketUtil;
import me.rina.rocan.client.event.network.PacketEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author SrRina
 * @since 23/02/2021 at 00:21
 **/
@Registry(name = "Critical", tag = "Critical", description = "Critical hits.", category = ModuleCategory.COMBAT)
public class ModuleCritical extends Module {
    public static ValueEnum settingMode = new ValueEnum("Mode", "Mode", "Critical action mode.", Mode.PACKET);

    public enum Mode {
        PACKET, JUMP;
    }

    @Listener
    public void onListen(PacketEvent.Send event) {
        if ((event.getPacket() instanceof CPacketUseEntity == false)) {
            return;
        }

        CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();

        boolean flag = mc.player.onGround && packet.getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(mc.world) instanceof EntityLivingBase;

        if (flag && KeyUtil.isPressed(mc.gameSettings.keyBindJump) == false) {
            switch ((Mode) settingMode.getValue()) {
                case JUMP: {
                    mc.player.jump();

                    break;
                }

                case PACKET: {
                    PacketUtil.send(new CPacketPlayer.Position(PlayerUtil.getPos()[0], PlayerUtil.getPos()[1] + 0.1f, PlayerUtil.getPos()[2], false));
                    PacketUtil.send(new CPacketPlayer.Position(PlayerUtil.getPos()[0], PlayerUtil.getPos()[1], PlayerUtil.getPos()[2], false));

                    break;
                }
            }
        }
    }
}
