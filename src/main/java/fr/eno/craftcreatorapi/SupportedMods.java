package fr.eno.craftcreatorapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum SupportedMods
{
    MINECRAFT("minecraft"),
    BOTANIA("botania"),
    THERMAL("thermal"),
    CREATE("create"),;

    private final String modId;

    public boolean isLoaded()
    {
        return CraftCreatorAPI.getInstance().getSupportedModsBase().isModLoaded(this.getModId());
    }

    public static SupportedMods getMod(String modId)
    {
        return Arrays.stream(values()).filter(mod -> mod.getModId().equals(modId)).findFirst().orElse(null);
    }

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
        return CraftCreatorAPI.getInstance().getSupportedModsBase().isModLoaded(CraftCreatorAPI.getKubeJsModId());
    }
}
