package fr.eno.craftcreator.serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import fr.eno.craftcreator.CraftCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;

@SuppressWarnings({"unused"})
public class CraftingTableRecipeSerializer
{
	private static final List<Character> keyList = Arrays.asList('#', 'E', 'X', 'M', '+', '$', '%', 'H', 'A');
	private static final File directory = new File(Minecraft.getInstance().gameDir, "Craft-Generator");
	private final static Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().serializeNulls().create();

	/*
	 * Made for Shapeless Recipes
	 */
	public static final void serializeShapelessRecipe(ItemStack output, List<Item> list)
	{
		File recipeFile = getRecipeFile(output);
		JsonObject recipe = new JsonObject();
		recipe.addProperty("type", "minecraft:crafting_shapeless");

		JsonArray ingredients = new JsonArray();

		for (Item item : list)
		{
			JsonObject obj = new JsonObject();
			obj.addProperty("item", item.getRegistryName().getPath());
			ingredients.add(obj);
		}

		recipe.add("ingredients", ingredients);
		JsonObject result = new JsonObject();
		result.addProperty("item", output.getItem().getRegistryName().toString());
		result.addProperty("count", output.getCount());
		recipe.add("result", result);

		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(recipeFile));
			gson.toJson(recipe, writer);
			Minecraft.getInstance().player.sendMessage(new StringTextComponent("Shapeless Recipe saved at : §a.minecraft" + recipeFile.getAbsolutePath().replace(Minecraft.getInstance().gameDir.getAbsolutePath(), "")));
			writer.flush();
			writer.close();
		} 
		catch (JsonIOException | IOException e)
		{
			CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
		}

	}

	/*
	 * Made for Shaped Recipes
	 */
	public static final void serializeShapedRecipe(ItemStack output, Map<Integer, Item> map)
	{
		File recipeFile = getRecipeFile(output);
		JsonObject recipe = new JsonObject();
		recipe.addProperty("type", "minecraft:crafting_shaped");
		Map<Item, Character> chars = createPattern(recipe, map);

		createKeys(recipe, chars);

		JsonObject result = new JsonObject();
		result.addProperty("item", output.getItem().getRegistryName().toString());
		result.addProperty("count", output.getCount());
		recipe.add("result", result);

		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(recipeFile));
			gson.toJson(recipe, writer);
			Minecraft.getInstance().player.sendMessage(new StringTextComponent("Shaped Recipe saved at : §a.minecraft" + recipeFile.getAbsolutePath().replace(Minecraft.getInstance().gameDir.getAbsolutePath(), "")));
			writer.flush();
			writer.close();
		} catch (JsonIOException | IOException e)
		{
			CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
		}

	}

	private static final Map<Item, Character> createPattern(JsonObject obj, Map<Integer, Item> map)
	{
		Map<Item, Character> chars = new HashMap<Item, Character>();
		JsonArray array = new JsonArray();

		int i = 0;
		for (int line = 0; line < 3; line++)
		{
			String str = "";

			for (int index = 0; index < 3; index++)
			{
				if (map.get(i) != null)
				{
					if (map.get(i) != Items.AIR)
					{
						if (!chars.containsKey(map.values().stream().collect(Collectors.toList()).get(i)))
							chars.put(map.get(i), keyList.get(i));

						str = str.concat(String.valueOf(chars.get(map.get(i))));
					} else
					{
						str = str.concat(" ");
					}
				} else
				{
					str = str.concat(" ");
				}

				i++;
			}

			array.add(str);
		}

		obj.add("pattern", array);

		return chars;
	}

	private static final void createKeys(JsonObject obj, Map<Item, Character> map)
	{
		JsonObject symbolListObj = new JsonObject();
		List<Item> list = map.keySet().stream().collect(Collectors.toList());

		for (int i = 0; i < list.size(); i++)
		{
			JsonObject symbolObj = new JsonObject();
			symbolObj.addProperty("item", String.valueOf(list.get(i).getRegistryName().toString()));
			symbolListObj.add(String.valueOf(map.get(list.get(i))), symbolObj);
		}

		obj.add("key", symbolListObj);
	}
	
	private static final File getRecipeFile(ItemStack stack)
	{
		directory.mkdirs();
		File recipeFile = new File(directory, stack.getItem().getRegistryName().getPath() + ".json");
		
		return recipeFile;
	}
}