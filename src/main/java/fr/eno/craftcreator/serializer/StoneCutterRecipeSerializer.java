package fr.eno.craftcreator.serializer;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.world.level.ItemLike;

public class StoneCutterRecipeSerializer extends RecipeSerializer
{
	private StoneCutterRecipeSerializer(ItemLike output, int count)
	{
		super(CraftType.STONECUTTING, output);
		this.setOutput(output, count);
	}

	public StoneCutterRecipeSerializer setIngredient(ItemLike item)
	{
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("item", item.asItem().getRegistryName().toString());
		recipe.add("ingredient", ingredient);
		return this;
	}

	private void setOutput(ItemLike output, int count)
	{
		recipe.addProperty("result", output.asItem().getRegistryName().toString());
		recipe.addProperty("count", count);
    }

	public static StoneCutterRecipeSerializer create(ItemLike output, int count)
	{
		return new StoneCutterRecipeSerializer(output, count);
	}
}