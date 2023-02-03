package fr.eno.craftcreator.recipes.utils;

import net.minecraftforge.fml.ModList;

public enum SupportedMods
{
    MINECRAFT("minecraft"),
    BOTANIA("botania"),
    THERMAL("thermal");

    private final String modId;

    SupportedMods(String modId)
    {
        this.modId = modId;
    }

    public String getModId()
    {
        return modId;
    }

    public boolean isLoaded()
    {
        return ModList.get().isLoaded(this.getModId());
    }

    public static boolean isModLoaded(String modId)
    {
        return ModList.get().isLoaded(modId);
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

        return SupportedMods.MINECRAFT;
    }

    public static boolean isKubeJSLoaded()
    {
        return ModList.get().isLoaded("kubejs");
    }
}