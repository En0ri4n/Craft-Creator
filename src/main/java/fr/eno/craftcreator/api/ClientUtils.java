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
    public static final KeyBinding KEY_OPEN_RECIPES_MENU = new KeyBinding("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");

    /**
     * @return the minecraft instance
     * @see Minecraft#getInstance()
     */
    public static Minecraft getMinecraft()
    {
        return Minecraft.getInstance();
    }

    /**
     * @return the client level instance
     * @see Minecraft#level
     */
    public static World getClientLevel()
    {
        return getMinecraft().level;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#font
     */
    public static FontRenderer getFont()
    {
        return getMinecraft().font;
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
	 * @see Minecraft#setScreen(Screen)
     */
    public static void openScreen(Screen screen)
    {
        getMinecraft().setScreen(screen);
    }

	/**
	 * @return the current displayed screen (can be null)
	 * @see Minecraft#screen
	 */
    public static Screen getCurrentScreen()
    {
        return getMinecraft().screen;
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
        getMinecraft().getTextureManager().bind(texture);
    }
}
