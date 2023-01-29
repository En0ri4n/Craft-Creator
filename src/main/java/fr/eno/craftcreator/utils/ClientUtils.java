package fr.eno.craftcreator.utils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

    public static final KeyMapping KEY_OPEN_RECIPES_MENU = new KeyMapping("key.craftcreator.open_recipes_menu", GLFW.GLFW_KEY_K, "key.categories.craft_creator");

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
     * @see Minecraft#level
     */
    public static Level getClientLevel()
    {
        return getMinecraft().level;
    }

    /**
     * @return the minecraft font
     * @see Minecraft#font
     */
    public static Font getFont()
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
    public static Player getClientPlayer()
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
}
