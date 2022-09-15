package fr.eno.craftcreator.serializer;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class SmithingTableRecipeSerializer extends RecipeSerializer
{
	private SmithingTableRecipeSerializer(ItemLike output)
	{
		super(CraftType.SMITHING_TABLE, output);
		this.setOutput(output);
	}

	public SmithingTableRecipeSerializer setIngredient(List<Item> list)
	{
		JsonObject base = new JsonObject();
		base.addProperty("item", list.get(0).getRegistryName().toString());
		
		JsonObject addition = new JsonObject();
		addition.addProperty("item", list.get(1).getRegistryName().toString());
		
		recipe.add("base", base);
		recipe.add("addition", addition);
		return this;
	}

	private void setOutput(ItemLike output)
	{
		JsonObject result = new JsonObject();
		result.addProperty("item", output.asItem().getRegistryName().toString());
		recipe.add("result", result);
    }

	public static SmithingTableRecipeSerializer create(ItemLike output)
	{
		return new SmithingTableRecipeSerializer(output);
	}
}