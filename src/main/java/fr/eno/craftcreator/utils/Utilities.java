package fr.eno.craftcreator.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class Utilities
{
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
	
	public static ITextComponent createClickableComponent(ITextComponent component, File toOpen)
	{
		return component.applyTextStyle(TextFormatting.UNDERLINE).applyTextStyle((msg) -> msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
	}
}