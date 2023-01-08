package fr.eno.craftcreator.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

// TODO replace all calls to minecraft by this class (except in screens classes)
public class ClientUtils
{
	private static final Minecraft mc = Minecraft.getInstance();
	
	public static void closeAndGrabMouse()
	{
		close();
		getMinecraft().mouseHandler.grabMouse();
	}

	public static Minecraft getMinecraft()
	{
		return mc;
	}

	public static Level getClientLevel()
	{
		return mc.level;
	}

	public static ItemRenderer getItemRenderer()
	{
		return getMinecraft().getItemRenderer();
	}

	public static Player getClientPlayer()
	{
		return getMinecraft().player;
	}

	public static void openScreen(Screen screen)
	{
		getMinecraft().setScreen(screen);
	}

	public static Screen getCurrentScreen()
	{
		return getMinecraft().screen;
	}

	public static void close()
	{
		mc.setScreen(null);
	}
	
	public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
	{
		return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
	}
}
