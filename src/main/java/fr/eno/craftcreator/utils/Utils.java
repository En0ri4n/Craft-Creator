package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author En0ri4n <br>
 * @apiNote Class for call method with the smallest size possible
 */
public class Utils
{
    private static final Random RANDOM = new Random();

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Verify that the object is not null.<br>
     * Calls {@link Objects#requireNonNull(Object)} with the object
     *
     * @param obj The object to verify
     * @param <T> the type of the object
     * @return the object
     * @see Objects#requireNonNull(Object)
     */
    public static <T> T notNull(T obj)
    {
        return Objects.requireNonNull(obj);
    }

    /**
     * calls {@link net.minecraft.client.gui.FontRenderer#width(String)}
     *
     * @param str The string
     * @return width of the given string
     * @see net.minecraft.client.gui.FontRenderer#width(String)
     */
    public static int width(String str)
    {
        return ClientUtils.getFont().width(str);
    }

    /**
     * Sort a list of entry with the specified token
     *
     * @param token  the string to found
     * @param inputs a list of entries
     * @return a non-null list with entries which match the token
     */
    public static List<SimpleListWidget.Entry> copyPartialMatches(String token, List<SimpleListWidget.Entry> inputs)
    {
        List<SimpleListWidget.Entry> list = new ArrayList<>();
        if(!token.isEmpty())
        {
            for(SimpleListWidget.Entry s : inputs)
            {
                if(s.toString().contains(token))
                {
                    list.add(s);
                }
            }
        }

        return list;
    }

    /**
     * Get the container's texture with the specified name
     *
     * @param mod The mod of the container
     * @param path  The path of the texture
     * @return the texture's resource location
     */
    public static ResourceLocation getGuiContainerTexture(SupportedMods mod, String path)
    {
        return References.getLoc("textures/gui/container/" + mod.getModId() + "/" + path);
    }

    /**
     * Split a string with the specified size
     *
     * @param text the string to split
     * @param size the size to split
     * @return a non-null list with the split string
     */
    public static List<String> splitToListWithSize(String text, int size)
    {
        List<String> parts = new ArrayList<>();

        int length = text.length();

        for(int i = 0; i < length; i += size)
        {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }

        return parts;
    }

    /**
     * Create a clickable component which open the specified file
     *
     * @param component the component to display
     * @param toOpen    the file to open
     * @return the clickable component
     */
    public static IFormattableTextComponent createComponentFileOpener(ITextComponent component, File toOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
    }

    /**
     * Create a clickable component which open the specified url
     *
     * @param component the component to display
     * @param urlToOpen the url to open
     * @return the clickable component
     */
    public static IFormattableTextComponent createComponentUrlOpener(ITextComponent component, String urlToOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlToOpen)));
    }

    public static String generateString(int size)
    {
        String output = "";

        for(int i = 0; i < size; i++)
        {
            output = output.concat(String.valueOf(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()))));
        }

        return output;
    }
}