package me.rina.rocan;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.event.management.EventManager;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.preset.management.PresetManager;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.client.command.CommandPrefix;
import me.rina.rocan.client.command.CommandToggle;
import me.rina.rocan.client.gui.GUI;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.module.exploit.ModuleXCarry;
import me.rina.rocan.client.module.misc.ModuleAutoFish;
import me.rina.rocan.client.module.misc.ModuleAutoRespawn;
import me.rina.rocan.client.module.misc.ModuleChatSuffix;
import me.rina.rocan.client.module.render.ModuleBlockHighlight;
import me.rina.rocan.client.module.render.ModuleHoleESP;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import team.stiff.pomelo.impl.annotated.AnnotatedEventManager;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
@Mod(modid = "rocan", name = Rocan.NAME, version = Rocan.VERSION)
public class Rocan {
    @Mod.Instance
    public static Rocan INSTANCE;

    public static final String NAME        = "Rocan";
    public static final String VERSION     = "0.1.7";
    public static final String PATH_CONFIG = "Rocan/";
    public static final String CHAT        = ChatFormatting.GRAY + "Rocan " + ChatFormatting.WHITE;

    /*
     * We create one final Minecraft, there is the function Minecraft or this variable;
     */
    public static final Minecraft MC = Minecraft.getMinecraft();

    /*
     * The event manager of team pomelo!!
     */
    private team.stiff.pomelo.EventManager pomeloEventManager = new AnnotatedEventManager();

    /* All managers of the client. */
    private ModuleManager moduleManager;
    private EventManager clientEventManager;
    private CommandManager commandManager;
    private SocialManager socialManager;
    private PresetManager presetManager;

    private ModuleClickGUI moduleClickGUI;
    private GUI wrapperGUI;

    /**
     * Registry all components.
     */
    public void onRegistry() {
        // Category Client.
        this.moduleManager.registry(new me.rina.rocan.client.module.client.ModuleClickGUI());

        // Category Render.
        this.moduleManager.registry(new ModuleBlockHighlight());
        this.moduleManager.registry(new ModuleHoleESP());

        // Category Misc.
        this.moduleManager.registry(new ModuleAutoRespawn());
        this.moduleManager.registry(new ModuleAutoFish());
        this.moduleManager.registry(new ModuleChatSuffix());

        // Exploit.
        this.moduleManager.registry(new ModuleXCarry());

        // Commands.
        this.commandManager.registry(new CommandPrefix());
        this.commandManager.registry(new CommandToggle());
    }

    /**
     * Method non-static to init the client.
     */
    public void onInitClient() {
        this.presetManager.onLoad();

        // We start here the GUI, cause, all settings and modules are loaded.
        this.moduleClickGUI = new ModuleClickGUI();
        this.moduleClickGUI.init();

        // Reload method to refresh states and values.
        PresetManager.reload();
        ModuleManager.reload();
    }

    /**
     * Method static to end client, save or disable something.
     */
    public static void onEndClient() {
        // Finish the preset saving all.
        Rocan.getModuleManager().onSave();
        Rocan.getSocialManager().onSave();

        PresetManager.INSTANCE.onSave();
    }

    @Mod.EventHandler
    public void onClientStarted(FMLInitializationEvent event) {
        this.moduleManager = new ModuleManager();
        this.clientEventManager = new EventManager();
        this.commandManager = new CommandManager();
        this.socialManager = new SocialManager();
        this.presetManager = new PresetManager();

        this.wrapperGUI = new GUI();

        MinecraftForge.EVENT_BUS.register(this.clientEventManager);
        MinecraftForge.EVENT_BUS.register(this.commandManager);

        this.onRegistry();
        this.onInitClient();
    }

    public static team.stiff.pomelo.EventManager getPomeloEventManager() {
        return INSTANCE.pomeloEventManager;
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

    public static ModuleClickGUI getModuleClickGUI() {
        return INSTANCE.moduleClickGUI;
    }

    public static SocialManager getSocialManager() {
        return INSTANCE.socialManager;
    }

    public static GUI getWrapperGUI() {
        return INSTANCE.wrapperGUI;
    }

    public static Minecraft getMinecraft() {
        return MC;
    }
}