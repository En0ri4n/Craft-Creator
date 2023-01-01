package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utilities
{
	public static List<SimpleListWidget.Entry> copyPartialMatches(String token, List<SimpleListWidget.Entry> inputs)
	{
		List<SimpleListWidget.Entry> list = new ArrayList<>();
		if (!token.isEmpty())
		{
			for (SimpleListWidget.Entry s : inputs)
			{
				if (s.toString().contains(token))
				{
					list.add(s);
				}
			}
		}

		return list;
	}

	public static ResourceLocation getGuiContainerTexture(String modid, String path)
	{
		return References.getLoc("textures/gui/container/" + modid + "/" + path);
	}

	public static List<String> splitToListWithSize(String text, int size)
	{
		List<String> parts = new ArrayList<>();

		int length = text.length();
		
		for (int i = 0; i < length; i += size)
		{
			parts.add(text.substring(i, Math.min(length, i + size)));
		}
		
		return parts;
	}
	
	public static MutableComponent createComponentFileOpener(Component component, File toOpen)
	{
		return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
	}

	public static MutableComponent createComponentUrlOpener(Component component, String urlToOpen)
	{
		return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlToOpen)));
	}
}