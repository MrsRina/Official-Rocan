package me.rina.rocan.api.event.management;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.render.Render2DEvent;
import me.rina.rocan.client.event.render.Render3DEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author SrRina
 * @since 15/11/20 at 7:45pm
 */
public class EventManager {
    private float currentRender2DPartialTicks;
    private float currentRender3DPartialTicks;

    private Color currentRGBColor;

    protected void setCurrentRender2DPartialTicks(float currentRender2DPartialTicks) {
        this.currentRender2DPartialTicks = currentRender2DPartialTicks;
    }

    public float getCurrentRender2DPartialTicks() {
        return currentRender2DPartialTicks;
    }

    protected void setCurrentRender3DPartialTicks(float currentRender3DPartialTicks) {
        this.currentRender3DPartialTicks = currentRender3DPartialTicks;
    }

    public float getCurrentRender3DPartialTicks() {
        return currentRender3DPartialTicks;
    }

    private void setCurrentRGBColor(Color currentRGBColor) {
        this.currentRGBColor = currentRGBColor;
    }

    public Color getCurrentRGBColor() {
        return currentRGBColor;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) return;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Rocan.MC.player == null) {
            return;
        }

        Rocan.EVENT_BUS.dispatch(new ClientTickEvent());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (Rocan.MC.player == null) {
            return;
        }

        this.setCurrentRender2DPartialTicks(event.getPartialTicks());

        Rocan.EVENT_BUS.dispatch(new Render2DEvent(event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (Rocan.MC.player == null) {
            return;
        }

        float[] currentSystemCycle = new float[] {
                (System.currentTimeMillis() % (360f / 32f) * (360f / 32f))
        };

        int currentColorCycle = Color.HSBtoRGB(currentSystemCycle[0], 1, 1);

        this.currentRGBColor = new Color(((currentColorCycle >> 16) & 0xFF), ((currentColorCycle >> 8) & 0xFF), (currentColorCycle & 0xFF));

        this.setCurrentRender3DPartialTicks(event.getPartialTicks());

        Rocan.EVENT_BUS.dispatch(new Render3DEvent(event.getPartialTicks()));

        /*
         * Basically the ticks are more smooth in event RenderWorldLastEvent;
         * This make any color update as the color fully smooth.
         * And we update the colors of the GUI too.
         */
        Rocan.getWrapperGUI().onUpdateColor();
    }

    @SubscribeEvent(priority = EventPriority.HIGH, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            Rocan.getModuleManager().onKeyPressed(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
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
