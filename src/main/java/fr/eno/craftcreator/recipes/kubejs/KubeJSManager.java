package fr.eno.craftcreator.recipes.kubejs;

import fr.eno.craftcreator.base.SupportedMods;
import net.minecraftforge.fml.loading.FMLLoader;

import java.io.File;

public class KubeJSManager
{
    private static boolean initialized = false;
    private static KubeJSManager INSTANCE;

    private KubeJSManager()
    {
        INSTANCE = this;
    }

    public File getKubeJSRecipesFolder()
    {
        check();
        File folder = new File(getKubeJSFolder(), "server_scripts");

        if(!folder.exists()) folder.mkdirs();

        return folder;
    }

    public File getKubeJSFolder()
    {
        check();
        return createKubeJSFolder();
    }

    private File createKubeJSFolder()
    {
        check();
        final File folder = new File(FMLLoader.getGamePath().toFile(), "kubejs");

        if(!folder.exists()) folder.mkdirs();

        return folder;
    }

    public static void initialize()
    {
        if(SupportedMods.isKubeJSLoaded()) initialized = true;

        if(initialized)
        {
            new KubeJSManager();
        }
    }

    public static boolean isInitialized()
    {
        return initialized;
    }

    public static KubeJSManager getInstance()
    {
        check();
        return INSTANCE;
    }

    private static void check()
    {
        if(!isInitialized())
            throw new RuntimeException("The mod KubeJS is not installed! (if you're seeing this, this is a bug!)");
    }
}
