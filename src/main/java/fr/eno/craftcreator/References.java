package fr.eno.craftcreator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class References
{
	public static final String MOD_ID = "craftcreator";
	public static final String MOD_NAME = "Craft Creator";
	public static final String VERSION = "0.0.1";
	
	public static Component getTranslate(String path, Object... args)
	{
		return new TranslatableComponent(MOD_ID + "." + path, args);
	}

	public static ResourceLocation getLoc(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}