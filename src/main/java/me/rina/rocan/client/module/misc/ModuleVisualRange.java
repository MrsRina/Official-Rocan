package me.rina.rocan.client.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.util.ArrayList;
import java.util.List;

import static me.rina.rocan.Rocan.MC;

public class ModuleVisualRange extends Module {

    private List<String> people;

    public ModuleVisualRange() {
        super("Visual Range","VisualRange","VisualRange", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        people = new ArrayList<>();
    }

    @Listener
    public void onUpdate(ClientTickEvent event) {
        if (mc.world == null | mc.world == null) {
            return;
        }
        List<String> players = new ArrayList<>();
        List<EntityPlayer> playerEntities = mc.world.playerEntities;

        for (Entity e : playerEntities) {
            if (e.getName().equals(MC.player.getName())) continue;
            players.add(e.getName());
        }

        if (players.size() > 0) {
            for (String name : players) {
                if (!people.contains(name)) {
                    ChatUtil.print(ChatFormatting.GRAY + name + ChatFormatting.RESET + "Is now your visual range");
                    }
                    people.add(name);
                }
            }
        }

    }
