package fr.eno.craftcreator.serializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.eno.craftcreator.utils.CraftType;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;

public class CraftingTableRecipeSerializer extends RecipeSerializer
{
	private static final List<Character> keyList = Arrays.asList('G', 'E', 'X', 'M', 'B', 'D', 'W', 'O', 'A');

	private CraftingTableRecipeSerializer(CraftType type, IItemProvider output, int count)
	{
		super(type, output);
		this.setOutput(output, count);
	}

	public CraftingTableRecipeSerializer setIngredients(List<Item> list)
	{
		if(type.equals(CraftType.CRAFTING_TABLE_SHAPED))
		{
			createShapedIngredients(list);
		}
		else
		{
			createShapelessIngredients(list);
		}

		return this;
	}

	private void createShapelessIngredients(List<Item> items)
	{
		JsonArray ingredients = new JsonArray();

		for (Item item : items)
		{
			JsonObject obj = new JsonObject();
			obj.addProperty("item", item.getRegistryName().getPath());
			ingredients.add(obj);
		}

		recipe.add("ingredients", ingredients);
	}

	private void createShapedIngredients(List<Item> items)
	{
		Map<Item, Character> pattern = createPattern(items);
		createKeys(pattern);
	}

	private void createKeys(Map<Item, Character> map)
	{
		JsonObject symbolListObj = new JsonObject();
		List<Item> list = map.keySet().stream().collect(Collectors.toList());

		for (int i = 0; i < list.size(); i++)
		{
			JsonObject symbolObj = new JsonObject();
			symbolObj.addProperty("item", String.valueOf(list.get(i).getRegistryName().toString()));
			symbolListObj.add(String.valueOf(map.get(list.get(i))), symbolObj);
		}

		recipe.add("key", symbolListObj);
	}

	private Map<Item, Character> createPattern(List<Item> list)
	{
		Map<Item, Character> patterns = new HashMap<Item, Character>();
		JsonArray array = new JsonArray();

		String str = "";

		for (int index = 0; index < 9; index++)
		{
			if(list.get(index) != null)
			{
				if(list.get(index) != Items.AIR)
				{
					if(!patterns.containsKey(list.get(index)))
						patterns.put(list.get(index), keyList.get(index));

					str = str.concat(String.valueOf(patterns.get(list.get(index))));
					continue;
				}
			}

			str = str.concat(" ");
		}

		Utilities.splitToListWithSize(str, 3).forEach(s -> array.add(s));
		recipe.add("pattern", array);

		return patterns;
	}

	private CraftingTableRecipeSerializer setOutput(IItemProvider output, int count)
	{
		JsonObject result = new JsonObject();
		result.addProperty("item", output.asItem().getRegistryName().toString());
		result.addProperty("count", count);
		recipe.add("result", result);

		return this;
	}

	public static CraftingTableRecipeSerializer create(CraftType type, IItemProvider output, int count)
	{
		return new CraftingTableRecipeSerializer(type, output, count);
	}
}