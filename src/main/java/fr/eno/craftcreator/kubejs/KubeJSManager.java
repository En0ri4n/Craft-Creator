package fr.eno.craftcreator.kubejs;

import fr.eno.craftcreator.kubejs.utils.*;
import net.minecraft.client.*;

import java.io.*;

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
        File folder = new File(Minecraft.getInstance().gameDir, "kubejs");

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
        if(!isInitialized()) try
        {
            throw new Exception("The mod KubeJS is not installed !");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
