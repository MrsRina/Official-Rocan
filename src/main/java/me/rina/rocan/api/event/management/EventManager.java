package me.rina.rocan.api.event.management;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.command.Command;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.module.movement.ModuleNoSlowDown;
import me.rina.turok.render.opengl.TurokGL;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 15/11/20 at 7:45pm
 */
public class EventManager {
    private float currentRender2DPartialTicks;
    private float currentRender3DPartialTicks;

    private int[] currentRGBColor = {0, 0, 0};

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

    private void setCurrentRGBColor(int[] currentRGBColor) {
        this.currentRGBColor = currentRGBColor;
    }

    public int[] getCurrentRGBColor() {
        return currentRGBColor;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) return;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtil.isPlayer()) {
            return;
        }

        Rocan.getSpammerManager().onUpdateAll();
        Rocan.getTrackerManager().onUpdateAll();
        Rocan.getPlayerServerManager().onUpdateAll();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (Rocan.MC.player == null) {
            return;
        }

        Rocan.getPomeloEventManager().dispatchEvent(event);

        this.setCurrentRender2DPartialTicks(event.getPartialTicks());

        for (Module modules : Rocan.getModuleManager().getModuleList()) {
            modules.onRender2D();

            TurokGL.pushMatrix();

            TurokGL.enable(GL11.GL_TEXTURE_2D);
            TurokGL.enable(GL11.GL_BLEND);

            GlStateManager.enableBlend();

            TurokGL.popMatrix();

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }

        float[] currentSystemCycle = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32f)
        };

        int currentColorCycle = Color.HSBtoRGB(currentSystemCycle[0], 1, 1);

        this.currentRGBColor = new int[] {
                ((currentColorCycle >> 16) & 0xFF),
                ((currentColorCycle >> 8) & 0xFF),
                (currentColorCycle & 0xFF)
        };

        this.setCurrentRender3DPartialTicks(event.getPartialTicks());

        /*
         * Basically the ticks are more smooth in event RenderWorldLastEvent;
         * This make any color update as the color fully smooth.
         * And we update the colors of the GUI too.
         */
        Rocan.getWrapper().onUpdateColor();

        for (Module modules : Rocan.getModuleManager().getModuleList()) {
            if (modules.isEnabled()) {
                modules.onRender3D();
            }

            modules.onSetting();
        }
    }

    @SubscribeEvent()
    public void onInputUpdate(InputUpdateEvent event) {
        Rocan.getPomeloEventManager().dispatchEvent(event);
    }

    @SubscribeEvent
    public void onPlayerSPPushOutOfBlocksEvent(PlayerSPPushOutOfBlocksEvent event) {
        Rocan.getPomeloEventManager().dispatchEvent(event);
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

            boolean isCommand = false;

            for (Command commands : Rocan.getCommandManager().getCommandList()) {
                Command commandRequested = CommandManager.get(args[0]);

                if (commandRequested != null) {
                    commandRequested.onCommand(args);

                    isCommand = true;

                    break;
                }
            }

            if (isCommand == false) {
                ChatUtil.print(Rocan.CHAT + ChatFormatting.RED + "Unknown command. Try help for a list commands");
            }
        }
    }
}
