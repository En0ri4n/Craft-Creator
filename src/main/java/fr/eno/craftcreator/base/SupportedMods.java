package fr.eno.craftcreator.base;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this enum is used to list all the mods supported by Craft-Creator
 * and if they are loaded or not
 */
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

    public List<ResourceLocation> getSupportedRecipeTypes()
    {
        return Arrays.stream(ModRecipeCreator.values()).filter(recipeCreator -> recipeCreator.getMod().equals(this)).map(ModRecipeCreator::getRecipeTypeLocation).collect(Collectors.toList());
    }

    public static List<SupportedMods> getSupportedLoadedMods()
    {
        return Arrays.stream(SupportedMods.values()).filter(SupportedMods::isLoaded).collect(Collectors.toList());
    }

    public static boolean isKubeJSLoaded()
    {
        return ModList.get().isLoaded("kubejs");
    }
}
