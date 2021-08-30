package fr.eno.craftcreator.serializer;

import com.google.gson.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraftforge.registries.*;

import java.util.*;

public class CraftingTableRecipeSerializer extends RecipeSerializer
{
	private static final List<Character> keyList = Arrays.asList('G', 'E', 'X', 'M', 'B', 'D', 'W', 'O', 'A');

	private CraftingTableRecipeSerializer(CraftType type, IItemProvider output, int count)
	{
		super(type, output);
		this.setOutput(output, count);
	}

	public void setIngredients(List<Item> list, Map<Slot, ResourceLocation> taggedSlot)
	{
		if(type.equals(CraftType.CRAFTING_TABLE_SHAPED))
		{
			createShapedIngredients(list, taggedSlot);
		}
		else
		{
			createShapelessIngredients(list);
		}

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

	private void createShapedIngredients(List<Item> items, Map<Slot, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, Character> pattern = createPattern(items, taggedSlot);
		createKeys(pattern);
	}

	private void createKeys(Map<ResourceLocation, Character> map)
	{
		JsonObject symbolListObj = new JsonObject();
		List<ResourceLocation> list = new ArrayList<>(map.keySet());

		for(ResourceLocation resourceLocation : list)
		{
			JsonObject symbolObj = new JsonObject();

			if(ItemTags.getCollection().get(resourceLocation) != null)
			{
				Tag<Item> tag = ItemTags.getCollection().get(resourceLocation);
				symbolObj.addProperty("tag", tag.getId().toString());
				symbolListObj.add(String.valueOf(map.get(tag.getId())), symbolObj);
			}
			else if(ForgeRegistries.ITEMS.containsKey(resourceLocation))
			{
				Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
				symbolObj.addProperty("item", item.getRegistryName().toString());
				symbolListObj.add(String.valueOf(map.get(item.getRegistryName())), symbolObj);
			}
		}

		recipe.add("key", symbolListObj);
	}

	private Map<ResourceLocation, Character> createPattern(List<Item> list, Map<Slot, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, Character> patterns = new HashMap<>();
		JsonArray array = new JsonArray();

		String str = "";

		for (int index = 0; index < 9; index++)
		{
			Character key = keyList.get(index);

			if(list.get(index) != null)
			{
				if(list.get(index) != Items.AIR)
				{
					int finalIndex = index;

					Optional<Slot> optionalSlot = taggedSlot.keySet().stream().filter(s -> s.getSlotIndex() == finalIndex).findFirst();

					if(optionalSlot.isPresent())
					{
						ResourceLocation loc = taggedSlot.get(optionalSlot.get());

						if(!patterns.containsKey(loc))
						{
							patterns.put(loc, key);
						}

						str = str.concat(String.valueOf(patterns.get(loc)));
						continue;
					}

					if(!patterns.containsKey(list.get(index).getRegistryName()))
					{
						patterns.put(list.get(index).getRegistryName(), keyList.get(index));
					}

					str = str.concat(String.valueOf(patterns.get(list.get(index).getRegistryName())));
					continue;
				}
			}

			str = str.concat(" ");
		}

		Utilities.splitToListWithSize(str, 3).forEach(array::add);
		recipe.add("pattern", array);

		return patterns;
	}

	private void setOutput(IItemProvider output, int count)
	{
		JsonObject result = new JsonObject();
		result.addProperty("item", output.asItem().getRegistryName().toString());
		result.addProperty("count", count);
		recipe.add("result", result);

	}

	public static CraftingTableRecipeSerializer create(CraftType type, IItemProvider output, int count)
	{
		return new CraftingTableRecipeSerializer(type, output, count);
	}
}