package fr.eno.craftcreator.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.CraftType;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class CraftingTableRecipeSerializer extends RecipeSerializer
{
	// Keys for the shaped recipe
	private static final List<Character> keyList = Arrays.asList('G', 'E', 'X', 'M', 'B', 'D', 'W', 'O', 'A');

	/**
	 *
	 * @param type The type of the recipe (shaped or shapeless)
	 * @param output The output of the recipe
	 * @param count The number of output
	 */
	private CraftingTableRecipeSerializer(CraftType type, ItemLike output, int count)
	{
		super(type, output);
		this.setOutput(output, count);
	}

	/**
	 * set the ingredient of the recipe
	 * @param list The list of the ingredient
	 * @param taggedSlot The list of the tagged slots (for tags)
	 */
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
			if(item.equals(Items.AIR)) continue;

			JsonObject obj = new JsonObject();
			obj.addProperty("item", item.getRegistryName().getPath());
			ingredients.add(obj);
		}

		recipe.add("ingredients", ingredients);
	}

	private void createShapedIngredients(List<Item> items, Map<SlotItemHandler, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, PairValues<Boolean, Character>> pattern = createPattern(items, taggedSlot);
		createKeys(pattern);
	}

	private void createKeys(Map<ResourceLocation, PairValues<Boolean, Character>> map)
	{
		JsonObject symbolListObj = new JsonObject();
		List<ResourceLocation> list = new ArrayList<>(map.keySet());

		for(ResourceLocation resourceLocation : list)
		{
			JsonObject symbolObj = new JsonObject();

			Optional<TagKey<Item>> optionalTag = ForgeRegistries.ITEMS.tags().getTagNames().filter(tag -> tag == ItemTags.create(resourceLocation)).findFirst();

			if(optionalTag.isPresent() && map.get(resourceLocation).getFirstValue())
			{
				TagKey<Item> tag = optionalTag.get();
				symbolObj.addProperty("tag", tag.location().toString());
				char symbol = map.get(tag.location()).getSecondValue();
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

	private Map<ResourceLocation, PairValues<Boolean, Character>> createPattern(List<Item> list, Map<SlotItemHandler, ResourceLocation> taggedSlot)
	{
		Map<ResourceLocation, PairValues<Boolean, Character>> patterns = new HashMap<>();
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
							patterns.put(loc, PairValues.create(true, key));
						}

						str = str.concat(String.valueOf(patterns.get(loc).getSecondValue()));
						continue;
					}

					if(!patterns.containsKey(list.get(index).getRegistryName()))
					{
						patterns.put(list.get(index).getRegistryName(), PairValues.create(false, keyList.get(index)));
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

	private void setOutput(ItemLike output, int count)
	{
		JsonObject result = new JsonObject();
		result.addProperty("item", output.asItem().getRegistryName().toString());
		result.addProperty("count", count);
		recipe.add("result", result);

	}

	public static CraftingTableRecipeSerializer create(CraftType type, ItemLike output, int count)
	{
		return new CraftingTableRecipeSerializer(type, output, count);
	}
}