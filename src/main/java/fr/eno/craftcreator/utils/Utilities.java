package fr.eno.craftcreator.utils;

import net.minecraft.util.text.*;
import net.minecraft.util.text.event.*;

import java.io.*;
import java.util.*;

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
	
	public static IFormattableTextComponent createClickableComponent(String component, File toOpen)
	{
		return new StringTextComponent(component).modifyStyle((msg) -> msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
	}
}