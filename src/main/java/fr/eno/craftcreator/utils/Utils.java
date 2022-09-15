package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.References;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * 
 * @author Eno_gamer10
 * 
 * @apiNote Class for call method with the smallest size possible
 *
 */
public class Utils
{
	static final Minecraft mc = Minecraft.getInstance();
	
	/**
	 * call {@link References#getTranslate(String, Object...)}
	 * @see References#getTranslate(String, Object...)
	 * 
	 * @return The {@link net.minecraft.network.chat.TranslatableComponent}
	 */
	public static Component get(String path, Object... args)
	{
		return References.getTranslate(path, args);
	}
	
	/**
	 * call {@link References#getTranslate(String, Object...)} directly formatted to string
	 * @see References#getTranslate(String, Object...)
	 * 
	 * @return The formatted string from {@link net.minecraft.network.chat.TranslatableComponent}
	 */
	public static String getS(String path, Object... args)
	{
		return References.getTranslate(path, args).getString();
	}
	
	/**
	 * call {@link net.minecraft.client.gui.Font#width(String)}
	 * @see net.minecraft.client.gui.Font#width(String)
	 * 
	 * @param str The string
	 * @return width of the given string
	 */
	public static int width(String str)
	{
		return mc.font.width(str);
	}
}