package fr.eno.craftcreator.serializer;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.util.IItemProvider;

public class StoneCutterRecipeSerializer extends RecipeSerializer
{
	private StoneCutterRecipeSerializer(IItemProvider output, int count)
	{
		super(CraftType.STONECUTTING, output);
		this.setOutput(output, count);
	}

	public StoneCutterRecipeSerializer setIngredient(IItemProvider item)
	{
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("item", item.asItem().getRegistryName().toString());
		recipe.add("ingredient", ingredient);
		return this;
	}

	private void setOutput(IItemProvider output, int count)
	{
		recipe.addProperty("result", output.asItem().getRegistryName().toString());
		recipe.addProperty("count", count);
    }

	public static StoneCutterRecipeSerializer create(IItemProvider output, int count)
	{
		return new StoneCutterRecipeSerializer(output, count);
	}
}