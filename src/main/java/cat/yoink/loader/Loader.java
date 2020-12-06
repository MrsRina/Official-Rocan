package cat.yoink.loader;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Main loader.
 *
 * @author yoink
 * @since 17/11/2020
 */
public enum Loader
{
    INSTANCE;

    private final Logger logger = LogManager.getLogger("Rocan");

    public void loadMixin()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.rocan.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    public void load()
    {
        logger.info("Initialization started");

        try
        {
            Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, byte[]> cache = (Map<String, byte[]>) field.get(Launch.classLoader);

            URL url = new URL(""); // TODO: 11/17/2020 Upload to server and add link.

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream inputStream = httpURLConnection.getInputStream();

            ZipInputStream zipInputStream = new ZipInputStream(inputStream);

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                String name = zipEntry.getName();

                if (!name.endsWith(".class")) continue;

                name = name.substring(0, name.length() - 6);
                name = name.replace('/', '.');

                ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
                int bytesRead;
                byte[] tempBuffer = new byte[16384];
                while ((bytesRead = zipInputStream.read(tempBuffer)) != -1)
                    streamBuilder.write(tempBuffer, 0, bytesRead);

                cache.put(name, streamBuilder.toByteArray());
            }
        }
        catch (Exception ignored) { }
    }
}
