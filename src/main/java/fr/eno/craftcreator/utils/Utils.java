package fr.eno.craftcreator.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.gui.screen.Screen;
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
    private static final ResourceLocation BUTTON_TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");
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

    /**
     * Generate a string with a given size
     *
     * @param size the size of the string
     * @return the string
     */
    public static String generateString(int size)
    {
        String output = "";

        for(int i = 0; i < size; i++)
        {
            output = output.concat(String.valueOf(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()))));
        }

        return output;
    }

    public static void renderSizedButton(MatrixStack matrixStack, int x, int y, int width, int height, boolean isActive, boolean isHovered)
    {
        ClientUtils.bindTexture(BUTTON_TEXTURE);

        int buttonTextureHeight = 20;
        int textureWidth = 100;
        int textureHeight = 60;
        int xTexture = 0;
        int yTexture = !isActive ? 0 : isHovered ? 20 : 40;

        renderSizedTexture(matrixStack, 4, x, y, width, height, xTexture, yTexture, textureWidth, textureHeight, buttonTextureHeight);
    }

    public static void renderSizedTexture(MatrixStack matrixStack, int cutSize, int x, int y, int width, int height, int xTexture, int yTexture, int textureWidth, int textureHeight, int buttonTextureHeight)
    {
        // Top left corner
        Screen.blit(matrixStack, x, y, cutSize, cutSize, xTexture, yTexture, cutSize, cutSize, textureWidth, textureHeight);
        // Left side
        Screen.blit(matrixStack, x, y + cutSize, cutSize, height - cutSize * 2, xTexture, yTexture + cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom left corner
        Screen.blit(matrixStack, x, y + height - cutSize, cutSize, cutSize, xTexture, yTexture + buttonTextureHeight - cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Top side
        Screen.blit(matrixStack, x + cutSize, y, width - cutSize * 2, cutSize, cutSize, yTexture, textureWidth - cutSize * 2, cutSize, textureWidth, textureHeight);
        // Top right corner
        Screen.blit(matrixStack, x + width - cutSize, y, cutSize, cutSize, textureWidth - cutSize, yTexture, cutSize, cutSize, textureWidth, textureHeight);
        // Right side
        Screen.blit(matrixStack, x + width - cutSize, y + cutSize, cutSize, height - cutSize * 2, textureWidth - cutSize, yTexture + cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom right corner
        Screen.blit(matrixStack, x + width - cutSize, y + height - cutSize, cutSize, cutSize, textureWidth - cutSize, yTexture + buttonTextureHeight - cutSize, cutSize, cutSize, textureWidth, textureHeight);
        // Bottom side
        Screen.blit(matrixStack, x + cutSize, y + height - cutSize, width - cutSize * 2, cutSize, cutSize, yTexture + buttonTextureHeight - cutSize, textureWidth - cutSize * 2, cutSize, textureWidth, textureHeight);
        // Middle
        Screen.blit(matrixStack, x + cutSize, y + cutSize, width - cutSize * 2, height - cutSize * 2, cutSize, yTexture + cutSize, textureWidth - cutSize * 2, buttonTextureHeight - cutSize * 2, textureWidth, textureHeight);
    }

    public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
    {
        return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
    }
}