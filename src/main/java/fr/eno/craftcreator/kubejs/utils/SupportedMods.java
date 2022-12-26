package fr.eno.craftcreator.kubejs.utils;

import net.minecraftforge.fml.ModList;

import java.util.List;

public enum SupportedMods
{
    KUBE_JS("kubejs"),
    MINECRAFT("minecraft"),
    BOTANIA("botania");

    private final String modId;

    SupportedMods(String modId)
    {
        this.modId = modId;
    }

    public static boolean isModLoaded(String modId)
    {
        return ModList.get().isLoaded(modId);
    }

    public String getModId()
    {
        return modId;
    }

    public static SupportedMods getMod(String modId)
    {
        for (SupportedMods mod : SupportedMods.values())
        {
            if (mod.getModId().equals(modId))
            {
                return mod;
            }
        }

        return null;
    }

    public boolean isLoaded()
    {
        return ModList.get().isLoaded(this.getModId());
    }

    public static boolean isKubeJSLoaded()
    {
        return ModList.get().isLoaded(KUBE_JS.getModId());
    }

}
