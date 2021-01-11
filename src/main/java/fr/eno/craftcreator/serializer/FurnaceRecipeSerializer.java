package fr.eno.craftcreator.serializer;

import com.google.gson.JsonObject;

import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class FurnaceRecipeSerializer extends RecipeSerializer
{	
	private double experience;
	private int cookingTime;

	private FurnaceRecipeSerializer(CraftType type, IItemProvider output)
	{
		super(type, output);
		this.setOutput(output);
		experience = 0.1D;
		cookingTime = 200;
	}
	
	public FurnaceRecipeSerializer setExperience(double exp)
	{
		recipe.addProperty("experience", exp);
		this.experience = exp;
		return this;
	}
	
	public FurnaceRecipeSerializer setCookingTime(int cookingTime)
	{
		recipe.addProperty("cookingtime", cookingTime);
		this.cookingTime = cookingTime;
		return this;
	}

	public FurnaceRecipeSerializer setIngredient(Item item)
	{
		JsonObject ingredient = new JsonObject();
		ingredient.addProperty("item", item.getRegistryName().toString());
		recipe.add("ingredient", ingredient);
		return this;
	}

	private FurnaceRecipeSerializer setOutput(IItemProvider output)
	{
		recipe.addProperty("result", output.asItem().getRegistryName().toString());
		return this;
	}
	
	@Override
	public void serializeRecipe()
	{
		this.setExperience(experience);
		this.setCookingTime(cookingTime);
		
		super.serializeRecipe();
	}

	public static FurnaceRecipeSerializer create(CraftType type, IItemProvider output)
	{
		return new FurnaceRecipeSerializer(type, output);
	}
}