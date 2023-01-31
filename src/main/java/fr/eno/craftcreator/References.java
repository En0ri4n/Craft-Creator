package fr.eno.craftcreator;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author En0ri4n <br>
 * <p>
 * This class contains all the references of the mod.
 */
@SuppressWarnings("unused")
public class References
{
    /**
     * The mod id
     */
    public static final String MOD_ID = "craftcreator";
    /**
     * The mod name
     */
    public static final String MOD_NAME = "Craft Creator";
    /**
     * The mod version
     */
    public static final String VERSION = "0.0.1";

    /**
     * Translate the given key to the current language.
     *
     * @param path the key to translate (the mod id will be automatically added at the beginning)
     * @param args the arguments to add to the translation
     * @return the translated key
     */
    public static IFormattableTextComponent getTranslate(String path, Object... args)
    {
        return new TranslationTextComponent(MOD_ID + "." + path, args);
    }

    /**
     * Get a resource location in the mod.
     *
     * @param path the path of the resource location
     * @return the resource location
     */
    public static ResourceLocation getLoc(String path)
    {
        return new ResourceLocation(MOD_ID, path);
    }
}