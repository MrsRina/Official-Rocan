package me.rina.rocan;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.api.command.management.CommandManager;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.management.ComponentManager;
import me.rina.rocan.api.event.management.EventManager;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.preset.management.PresetManager;
import me.rina.rocan.api.social.management.SocialManager;
import me.rina.rocan.api.tracker.management.TrackerManager;
import me.rina.rocan.client.command.*;
import me.rina.rocan.client.Wrapper;
import me.rina.rocan.client.component.*;
import me.rina.rocan.client.gui.component.ComponentClickGUI;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.manager.chat.SpammerManager;
import me.rina.rocan.client.manager.entity.EntityWorldManager;
import me.rina.rocan.client.manager.network.PlayerServerManager;
import me.rina.rocan.client.manager.world.HoleManager;
import me.rina.rocan.client.module.client.ModuleAntiCheat;
import me.rina.rocan.client.module.client.ModuleDeveloper;
import me.rina.rocan.client.module.client.ModuleHUD;
import me.rina.rocan.client.module.client.ModuleTPSSync;
import me.rina.rocan.client.module.combat.*;
import me.rina.rocan.client.module.exploit.*;
import me.rina.rocan.client.module.misc.*;
import me.rina.rocan.client.module.movement.*;
import me.rina.rocan.client.module.render.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import team.stiff.pomelo.impl.annotated.AnnotatedEventManager;

import java.util.Collections;
import java.util.Comparator;

/**
 * @author SrRina
 * @since 15/11/20 at 4:51pm
 */
@Mod(modid = "rocan", name = Rocan.NAME, version = Rocan.VERSION)
public class Rocan {
    @Mod.Instance
    public static Rocan INSTANCE;

    public static final String NAME        = "Rocan";
    public static final String VERSION     = "1.0beta";
    public static final String PATH_CONFIG = "Rocan/";
    public static final String CHAT        = ChatFormatting.BLUE + "Rocan " + ChatFormatting.WHITE;

    /*
     * We create one final Minecraft, there is the function Minecraft or this variable;
     */
    public static final Minecraft MC = Minecraft.getMinecraft();

    /*
     * The event manager of team pomelo!!
     */
    private team.stiff.pomelo.EventManager pomeloEventManager = new AnnotatedEventManager();

    /* API managers. */
    private TrackerManager trackerManager;
    private ModuleManager moduleManager;
    private EventManager clientEventManager;
    private CommandManager commandManager;
    private SocialManager socialManager;
    private PresetManager presetManager;
    private ComponentManager componentManager;

    /* Not API managers. */
    private SpammerManager spammerManager;
    private PlayerServerManager playerServerManager;
    private EntityWorldManager entityWorldManager;
    private HoleManager holeManager;

    /* GUI screen stuff. */
    private ModuleClickGUI moduleClickGUI;
    private ComponentClickGUI componentClickGUI;
    private Wrapper wrapper;

    /**
     * Registry all components.
     */
    public void onRegistry() {
        // Category Client.
        this.moduleManager.registry(new me.rina.rocan.client.module.client.ModuleClickGUI());
        this.moduleManager.registry(new ModuleHUD());
        this.moduleManager.registry(new ModuleDeveloper());
        this.moduleManager.registry(new ModuleTPSSync());
        this.moduleManager.registry(new ModuleAntiCheat());

        // Category Combat.
        this.moduleManager.registry(new ModuleOffhand());
        this.moduleManager.registry(new ModuleAutoArmour());
        this.moduleManager.registry(new ModuleSurround());
        this.moduleManager.registry(new ModuleKillAura());
        this.moduleManager.registry(new ModuleCritical());
        this.moduleManager.registry(new ModuleHoleFiller());

        // Category Render.
        this.moduleManager.registry(new ModuleBlockHighlight());
        this.moduleManager.registry(new ModuleHoleESP());
        this.moduleManager.registry(new ModuleFullBright());
        this.moduleManager.registry(new ModuleCustomCamera());
        this.moduleManager.registry(new ModuleNameTags());
        this.moduleManager.registry(new ModuleNoRender());
        this.moduleManager.registry(new ModuleFreecam());

        // Category Misc.
        this.moduleManager.registry(new ModuleAutoRespawn());
        this.moduleManager.registry(new ModuleAutoFish());
        this.moduleManager.registry(new ModuleChatSuffix());
        this.moduleManager.registry(new ModuleSpammer());
        this.moduleManager.registry(new ModuleAntiAFK());
        this.moduleManager.registry(new ModuleAutoEat());

        // Exploit.
        this.moduleManager.registry(new ModuleExtraSlots());
        this.moduleManager.registry(new ModuleFastUse());
        this.moduleManager.registry(new ModuleBetterMine());
        this.moduleManager.registry(new ModuleCancelPackets());
        this.moduleManager.registry(new ModuleAutoHat());
        this.moduleManager.registry(new ModuleBurrow());

        // Movement
        this.moduleManager.registry(new ModuleAutoWalk());
        this.moduleManager.registry(new ModuleMoveGUI());
        this.moduleManager.registry(new ModuleVelocity());
        this.moduleManager.registry(new ModuleStep());
        this.moduleManager.registry(new ModuleStrafe());
        this.moduleManager.registry(new ModuleNoSlowDown());

        // Commands.
        this.commandManager.registry(new CommandPrefix());
        this.commandManager.registry(new CommandToggle());
        this.commandManager.registry(new CommandCoords());
        this.commandManager.registry(new CommandSocial());
        this.commandManager.registry(new CommandVanish());
        this.commandManager.registry(new CommandSettings());

        // Components.
        this.componentManager.registry(new ComponentArmor());
        this.componentManager.registry(new ComponentCoordinates());
        this.componentManager.registry(new ComponentInventory());
        this.componentManager.registry(new ComponentWatermark());
        this.componentManager.registry(new ComponentWelcome());

        // We organize module list and component list to alphabetical order.
        this.moduleManager.getModuleList().sort(Comparator.comparing(Module::getName));
        this.componentManager.getComponentList().sort(Comparator.comparing(Component::getName));

        // Log.
        System.out.println("Rocan was successfully initialized.");
    }

    /**
     * Method non-static to init the client.
     */
    public void onInitClient() {
        // We start here the GUI, because, all settings and modules are loaded.
        this.moduleClickGUI = new ModuleClickGUI();
        this.moduleClickGUI.init();

        // Start the HUD editor component.
        this.componentClickGUI = new ComponentClickGUI();
        this.componentClickGUI.init();

        // Do load preset.
        PresetManager.reload();
    }

    /**
     * Method static to end client, save or disable something.
     */
    public static void shutdownClient() {
        me.rina.rocan.client.module.client.ModuleClickGUI moduleClickGUI = (me.rina.rocan.client.module.client.ModuleClickGUI) ModuleManager.get(me.rina.rocan.client.module.client.ModuleClickGUI.class);
        ModuleSpammer moduleSpammer = (ModuleSpammer) ModuleManager.get(ModuleSpammer.class);
        ModuleFreecam moduleFreecam = (ModuleFreecam) ModuleManager.get(ModuleFreecam.class);
        ModuleHUD moduleHUD = (ModuleHUD) ModuleManager.get(ModuleHUD.class);

        // Close some modules.
        moduleClickGUI.setDisabled();
        moduleSpammer.setDisabled();
        moduleFreecam.setDisabled();
        moduleHUD.setDisabled();

        // Save preset.
        PresetManager.shutdown();
    }

    @Mod.EventHandler
    public void onClientStarted(FMLInitializationEvent event) {
        this.trackerManager = new TrackerManager();
        this.moduleManager = new ModuleManager();
        this.clientEventManager = new EventManager();
        this.commandManager = new CommandManager();
        this.socialManager = new SocialManager();
        this.presetManager = new PresetManager();
        this.componentManager = new ComponentManager();
        this.spammerManager = new SpammerManager();
        this.playerServerManager = new PlayerServerManager();
        this.entityWorldManager = new EntityWorldManager();
        this.holeManager = new HoleManager();

        this.wrapper = new Wrapper();

        MinecraftForge.EVENT_BUS.register(this.clientEventManager);
        MinecraftForge.EVENT_BUS.register(this.commandManager);

        this.onRegistry();
        this.onInitClient();

        /*
         * Mixin is bad.
         */
        Runtime.getRuntime().addShutdownHook(new Thread("Rocan Shutdown Hook") {
            @Override
            public void run() {
                Rocan.shutdownClient();
            }
        });
    }

    public static team.stiff.pomelo.EventManager getPomeloEventManager() {
        return INSTANCE.pomeloEventManager;
    }

    public static TrackerManager getTrackerManager() {
        return INSTANCE.trackerManager;
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

    public static SocialManager getSocialManager() {
        return INSTANCE.socialManager;
    }

    public static PresetManager getPresetManager() {
        return INSTANCE.presetManager;
    }

    public static ComponentManager getComponentManager() {
        return INSTANCE.componentManager;
    }

    public static ModuleClickGUI getModuleClickGUI() {
        return INSTANCE.moduleClickGUI;
    }

    public static ComponentClickGUI getComponentClickGUI() {
        return INSTANCE.componentClickGUI;
    }

    public static SpammerManager getSpammerManager() {
        return INSTANCE.spammerManager;
    }

    public static PlayerServerManager getPlayerServerManager() {
        return INSTANCE.playerServerManager;
    }

    public static EntityWorldManager getEntityWorldManager() {
        return INSTANCE.entityWorldManager;
    }

    public static HoleManager getHoleManager() {
        return INSTANCE.holeManager;
    }

    public static Wrapper getWrapper() {
        return INSTANCE.wrapper;
    }

    public static Minecraft getMinecraft() {
        return MC;
    }
}