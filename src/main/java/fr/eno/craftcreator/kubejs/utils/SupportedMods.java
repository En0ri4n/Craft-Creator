package fr.eno.craftcreator.kubejs.utils;

import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.List;

public class SupportedMods
{
    public static final String KUBE_JS = "kubejs";

    public static final String MINECRAFT = "minecraft";
    public static final String BOTANIA = "botania";

    public static final List<String> MODS_IDS = Arrays.asList(MINECRAFT, BOTANIA);

    public static boolean isBotaniaLoaded()
    {
        return isKubeJSLoaded() && ModList.get().isLoaded(BOTANIA);
    }

    public static boolean isKubeJSLoaded()
    {
        return ModList.get().isLoaded(KUBE_JS);
    }
}
