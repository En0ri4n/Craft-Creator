package fr.eno.craftcreator.kubejs;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.utils.SupportedMods;

import java.io.File;

public class KubeJSManager
{
    private static boolean initialized = false;
    private static KubeJSManager INSTANCE;

    private final File kubeJSFolder;

    private KubeJSManager()
    {
        INSTANCE = this;
        kubeJSFolder = createKubeJSFolder();
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
        return kubeJSFolder;
    }

    private File createKubeJSFolder()
    {
        check();
        File folder = new File(ClientUtils.getMinecraft().gameDir, "kubejs");

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
