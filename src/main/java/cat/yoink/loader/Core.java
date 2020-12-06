package cat.yoink.loader;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Core mod. Gets loaded as soon as you launch the game. Not during mod initialization.
 *
 * @author yoink
 * @since 17/11/2020
 */
public final class Core implements IFMLLoadingPlugin
{ // TODO: 11/17/2020 Add some sort of verification. (In loader or client itself)
    public Core()
    {
//        Loader.INSTANCE.load(); /* Commented out because in dev environment */

        Loader.INSTANCE.loadMixin();
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;

        // TODO: 11/17/2020 Add AccessTransformer.
    }
}
