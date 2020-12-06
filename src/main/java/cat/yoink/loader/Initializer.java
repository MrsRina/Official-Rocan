package cat.yoink.loader;

import me.rina.rocan.Rocan;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Initializer for the mod. In dev environment and in client environment.
 *
 * @author yoink
 * @since 17/11/2020
 */
@Mod(modid = "rocan", name = "Rocan", version = "1")
public final class Initializer
{
    @Mod.EventHandler
    public void initialize(FMLInitializationEvent event)
    {
        Rocan.INSTANCE.onClientStarted(event);
    }
}
