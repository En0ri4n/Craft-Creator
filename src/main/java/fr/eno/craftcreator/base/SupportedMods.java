package fr.eno.craftcreator.base;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This enum is used to list all the mods supported by Craft-Creator,
 * and if they are loaded or not
 */
public enum SupportedMods
{
    MINECRAFT("minecraft"),
    BOTANIA("botania"),
    THERMAL("thermal"),
    CREATE("create"),;

    private final String modId;

    SupportedMods(String modId)
    {
        this.modId = modId;
    }

    /**
     * Get the mod id
     *
     * @return The mod id
     */
    public String getModId()
    {
        return modId;
    }

    /**
     * Check if the mod is loaded
     *
     * @return True if the mod is loaded, false otherwise
     * @see SupportedMods#isModLoaded(String)
     */
    public boolean isLoaded()
    {
        return isModLoaded(this.getModId());
    }

    /**
     * Check if a mod is loaded
     *
     * @param modId The mod id
     * @return True if the mod is loaded, false otherwise
     */
    public static boolean isModLoaded(String modId)
    {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Get the enum value from the mod id
     *
     * @param modId The mod id
     * @return The enum value (MINECRAFT if the mod is not supported, avoiding null value)
     */
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

    /**
     * Get the list of all the recipe types supported by the mod
     *
     * @return The list of all the recipe types supported by the mod
     */
    public List<ResourceLocation> getSupportedRecipeTypes()
    {
        return Arrays.stream(ModRecipeCreator.values()).filter(recipeCreator -> recipeCreator.getMod().equals(this)).map(ModRecipeCreator::getRecipeTypeLocation).collect(Collectors.toList());
    }

    /**
     * Get the list of all the supported mods loaded
     *
     * @return The list of all the supported mods loaded (MINECRAFT is always loaded)
     */
    public static List<SupportedMods> getSupportedLoadedMods()
    {
        return Arrays.stream(SupportedMods.values()).filter(SupportedMods::isLoaded).collect(Collectors.toList());
    }

    /**
     * Check if KubeJS is loaded
     *
     * @return True if KubeJS is loaded, false otherwise
     */
    public static boolean isKubeJSLoaded()
    {
        return ModList.get().isLoaded("kubejs");
    }
}
