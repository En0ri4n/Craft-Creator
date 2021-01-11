package fr.eno.craftcreator;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class References
{
	public static final String MOD_ID = "craftcreator";
	public static final String MOD_NAME = "Craft Creator";
	public static final String VERSION = "0.0.1";
	
	public static TranslationTextComponent getTranslate(String path, Object... args)
	{
		return new TranslationTextComponent(MOD_ID + "." + path, args);
	}

	public static ResourceLocation getLoc(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}