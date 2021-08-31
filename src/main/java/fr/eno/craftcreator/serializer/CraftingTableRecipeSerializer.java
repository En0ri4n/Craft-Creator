package fr.eno.craftcreator.serializer;

import com.google.gson.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.item.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraftforge.items.*;
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

	public void setIngredients(List<Item> list, Map<SlotItemHandler, ResourceLocation> taggedSlot)
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

	private void createShapedIngredients(List<Item> items, Map<SlotItemHandler, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, PairValue<Boolean, Character>> pattern = createPattern(items, taggedSlot);
		createKeys(pattern);
	}

	private void createKeys(Map<ResourceLocation, PairValue<Boolean, Character>> map)
	{
		JsonObject symbolListObj = new JsonObject();
		List<ResourceLocation> list = new ArrayList<>(map.keySet());

		for(ResourceLocation resourceLocation : list)
		{
			JsonObject symbolObj = new JsonObject();

			Optional<? extends ITag.INamedTag<Item>> optionalTag = ItemTags.getAllTags().stream().filter(tag -> tag.getName().equals(resourceLocation)).findFirst();

			if(optionalTag.isPresent() && map.get(resourceLocation).getFirstValue())
			{
				ITag.INamedTag<Item> tag = optionalTag.get();
				symbolObj.addProperty("tag", tag.getName().toString());
				char symbol = map.get(tag.getName()).getSecondValue();
				symbolListObj.add(String.valueOf(symbol), symbolObj);
			}
			else if(ForgeRegistries.ITEMS.containsKey(resourceLocation))
			{
				Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
				symbolObj.addProperty("item", item.getRegistryName().toString());
				char symbol = map.get(item.getRegistryName()).getSecondValue();
				symbolListObj.add(String.valueOf(symbol), symbolObj);
			}
		}

		recipe.add("key", symbolListObj);
	}

	private Map<ResourceLocation, PairValue<Boolean, Character>> createPattern(List<Item> list, Map<SlotItemHandler, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, PairValue<Boolean, Character>> patterns = new HashMap<>();
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

					Optional<SlotItemHandler> optionalSlot = taggedSlot.keySet().stream().filter(s -> s.getSlotIndex() == finalIndex).findFirst();

					if(optionalSlot.isPresent())
					{
						ResourceLocation loc = taggedSlot.get(optionalSlot.get());

						if(!patterns.containsKey(loc))
						{
							patterns.put(loc, PairValue.create(true, key));
						}

						str = str.concat(String.valueOf(patterns.get(loc).getSecondValue()));
						continue;
					}

					if(!patterns.containsKey(list.get(index).getRegistryName()))
					{
						patterns.put(list.get(index).getRegistryName(), PairValue.create(false, keyList.get(index)));
					}

					str = str.concat(String.valueOf(patterns.get(list.get(index).getRegistryName()).getSecondValue()));
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