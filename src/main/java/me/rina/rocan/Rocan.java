package me.rina.rocan;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.event.management.EventManager;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.client.command.CommandPrefix;
import me.rina.rocan.client.command.CommandToggle;
import me.rina.rocan.client.module.ModuleExample;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import team.stiff.pomelo.impl.annotated.AnnotatedEventManager;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
public enum Rocan {
    INSTANCE;

    public static final String NAME = "Rocan";
    public static final String VERSION = "0.0.1";
    public static final String CHAT = ChatFormatting.GOLD + "/* Rocan */ ";

    /*
     * We will use this event manager;
     */
    public static final team.stiff.pomelo.EventManager EVENT_BUS = new AnnotatedEventManager();

    /* All managers of the client. */
    private ModuleManager moduleManager;
    private EventManager clientEventManager;
    private CommandManager commandManager;

    public void onRegistry() {
        // Modules.
        this.moduleManager.registry(new ModuleExample());

        // Commands.
        this.commandManager.registry(new CommandPrefix());
        this.commandManager.registry(new CommandToggle());
    }

    @Mod.EventHandler
    public void onClientStarted(FMLInitializationEvent event) {
        this.moduleManager = new ModuleManager();
        this.clientEventManager = new EventManager();
        this.commandManager = new CommandManager();

        MinecraftForge.EVENT_BUS.register(this.clientEventManager);
        MinecraftForge.EVENT_BUS.register(this.commandManager);

        this.onRegistry();
    }

    public static ModuleManager getModuleManager() {
        return INSTANCE.moduleManager;
    }

    public static EventManager getClientEventManager() {
        return INSTANCE.clientEventManager;
    }

    public static CommandManager getCommandManager() {
        return INSTANCE.commandManager;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
}