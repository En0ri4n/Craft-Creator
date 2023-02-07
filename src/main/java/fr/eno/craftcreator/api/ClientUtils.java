package fr.eno.craftcreator.api;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author En0ri4n <br>
 * <p>
 * This class contains some useful methods for the client.
 */
public class ClientUtils
{
    public static final KeyBinding KEY_OPEN_RECIPES_MENU = new KeyBinding("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");

    private static final Supplier<Minecraft> minecraftSupplier = Minecraft::getInstance;

    /**
     * @return the minecraft instance
     * @see Minecraft#getInstance()
     */
    public static Minecraft getMinecraft()
    {
        return minecraftSupplier.get();
    }

    /**
     * @return the client level instance
     * @see Minecraft#level
     */
    public static World getClientLevel()
    {
        return minecraftSupplier.get().level;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#font
     */
    public static FontRenderer getFontRenderer()
    {
        return minecraftSupplier.get().font;
    }

	/**
     * @return the minecraft item renderer
     * @see Minecraft#getItemRenderer()
     */
    public static ItemRenderer getItemRenderer()
    {
        return minecraftSupplier.get().getItemRenderer();
    }

	/**
	 * @return the client minecraft player
	 * @see Minecraft#player
	 */
    public static PlayerEntity getClientPlayer()
    {
        return minecraftSupplier.get().player;
    }

	/**
	 * @param screen the screen to open
	 * @see Minecraft#setScreen(Screen)
     */
    public static void openScreen(Screen screen)
    {
        minecraftSupplier.get().setScreen(screen);
    }

	/**
	 * @return the current displayed screen (can be null)
	 * @see Minecraft#screen
	 */
    public static Screen getCurrentScreen()
    {
        return minecraftSupplier.get().screen;
    }

	/**
     * Register the given container with the given screen
     * @see net.minecraft.client.gui.ScreenManager#register(ContainerType, ScreenManager.IScreenFactory)
     */
    public static <M extends Container, U extends Screen & IHasContainer<M>> void registerScreen(ContainerType<? extends M> container, ScreenManager.IScreenFactory<M, U> screenConstructor)
    {
        ScreenManager.register(container, screenConstructor);
    }

    /**
     * Send to the client player a message in the chat
     * @see PlayerEntity#sendMessage(net.minecraft.util.text.ITextComponent, java.util.UUID)
     */
    public static void sendClientPlayerMessage(ITextComponent message)
    {
        getClientPlayer().sendMessage(message, getClientPlayer().getUUID());
    }

    public static void sendMessage(PlayerEntity player, ITextComponent message)
    {
        player.sendMessage(message, player.getUUID());
    }

    public static void bindTexture(ResourceLocation texture)
    {
        minecraftSupplier.get().getTextureManager().bind(texture);
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
        return getFontRenderer().width(str);
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
     * Parse a string to a resource location
     * @param location the string to parse
     * @return the resource location
     *
     * @see ResourceLocation#tryParse(String)
     */
    public static ResourceLocation parse(String location)
    {
        return ResourceLocation.tryParse(location);
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
}
