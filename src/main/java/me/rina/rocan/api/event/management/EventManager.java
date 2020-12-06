package me.rina.rocan.api.event.management;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.util.chat.ChatUtil;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author SrRina
 * @since 15/11/20 at 7:45pm
 */
public class EventManager {
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) return;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Rocan.getMinecraft().player == null) {
            return;
        }

        Rocan.getModuleManager().onUpdateModuleList();
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            Rocan.getModuleManager().onKeyBinding(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority =  EventPriority.NORMAL)
    public void onChat(ClientChatEvent event) {
        String message = event.getMessage();

        String currentPrefix = CommandManager.getCommandPrefix().getPrefix();

        if (message.startsWith(currentPrefix)) {
            event.setCanceled(true);

            ChatUtil.malloc(message);

            String[] args = Rocan.getCommandManager().split(message);

            boolean notCommand = true;

            for (Command commands : Rocan.getCommandManager().getCommandList()) {
                Command commandRequested = CommandManager.get(args[0]);

                if (commandRequested != null) {
                    commandRequested.onCommand(args);

                    notCommand = false;

                    break;
                }
            }

            if (notCommand) {
                ChatUtil.print(ChatFormatting.RED + "Unknown command.");
            }
        }
    }
}
