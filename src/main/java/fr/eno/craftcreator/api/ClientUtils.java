package fr.eno.craftcreator.api;

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

/**
 * @author En0ri4n <br>
 * <p>
 * This class contains some useful methods for the client side.
 */
// TODO replace all calls to minecraft by this class (except in screens classes)
public class ClientUtils
{
    private static final Minecraft mc = Minecraft.getInstance();

    public static final KeyBinding KEY_OPEN_RECIPES_MENU = new KeyBinding("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");

    /**
     * @return the minecraft instance
     * @see Minecraft#getInstance()
     */
    public static Minecraft getMinecraft()
    {
        return mc;
    }

    /**
     * @return the client level instance
     * @see Minecraft#world
     */
    public static World getClientLevel()
    {
        return getMinecraft().world;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#fontRenderer
     */
    public static FontRenderer getFont()
    {
        return getMinecraft().fontRenderer;
    }

	/**
     * @return the minecraft item renderer
     * @see Minecraft#getItemRenderer()
     */
    public static ItemRenderer getItemRenderer()
    {
        return getMinecraft().getItemRenderer();
    }

	/**
	 * @return the client minecraft player
	 * @see Minecraft#player
	 */
    public static PlayerEntity getClientPlayer()
    {
        return getMinecraft().player;
    }

	/**
	 * @param screen the screen to open
	 * @see Minecraft#displayGuiScreen(Screen)
	 */
    public static void openScreen(Screen screen)
    {
        getMinecraft().displayGuiScreen(screen);
    }

	/**
	 * @return the current displayed screen (can be null)
	 * @see Minecraft#currentScreen
	 */
    public static Screen getCurrentScreen()
    {
        return getMinecraft().currentScreen;
    }

	/**
	 * Check if the mouse is in the specified area
	 *
	 * @return True if the mouse is in the specified area
	 */
    public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
    {
        return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
    }

    /**
     * Register the given container with the given screen
     * @see net.minecraft.client.gui.ScreenManager#registerFactory(ContainerType, ScreenManager.IScreenFactory)
     */
    public static <M extends Container, U extends Screen & IHasContainer<M>> void registerScreen(ContainerType<? extends M> container, ScreenManager.IScreenFactory<M, U> screenConstructor)
    {
        ScreenManager.registerFactory(container, screenConstructor);
    }

    /**
     * Send to the client player a message in the chat
     * @see PlayerEntity#sendMessage(net.minecraft.util.text.ITextComponent, java.util.UUID)
     */
    public static void sendClientPlayerMessage(ITextComponent message)
    {
        getClientPlayer().sendMessage(message, getClientPlayer().getUniqueID());
    }

    public static void sendMessage(PlayerEntity player, ITextComponent message)
    {
        player.sendMessage(message, player.getUniqueID());
    }

    public static void bindTexture(ResourceLocation texture)
    {
        getMinecraft().getTextureManager().bindTexture(texture);
    }
}
