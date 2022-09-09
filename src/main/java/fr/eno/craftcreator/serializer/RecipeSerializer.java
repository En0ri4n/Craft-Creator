package fr.eno.craftcreator.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.utils.CraftType;
import fr.eno.craftcreator.utils.Utilities;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public abstract class RecipeSerializer
{
	private static final Minecraft mc = Minecraft.getInstance();
	private static final Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

	protected final CraftType type;
	protected final JsonObject recipe;
	
	private final IItemProvider output;
	
	public RecipeSerializer(CraftType type, IItemProvider output)
	{
		this.recipe = new JsonObject();
		this.type = type;
		this.setType(type);
		this.output = output;
	}

	private void setType(CraftType type)
	{
		recipe.addProperty("type", type.toString());
	}
	
	public void serializeRecipe(boolean isKubeJSRecipe)
	{
		if(this.output.asItem() == Items.AIR) return;

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.getOutputFile())))
		{
			if(!isKubeJSRecipe)
			{
				gson.toJson(recipe, writer);
				Objects.requireNonNull(mc.player).sendMessage(Utilities.createClickableComponent(Utils.getS("serializer.success", this.getOutputFile().getName()), this.getOutputFile()), mc.player.getUniqueID());
			}
			else
			{
				IRecipeType<?> recipeType = RecipeFileUtils.byName(type.getType());

				if(type.equals(CraftType.CRAFTING_TABLE_SHAPED) || type.equals(CraftType.CRAFTING_TABLE_SHAPELESS))
					recipeType = RecipeFileUtils.byName(new ResourceLocation("minecraft", "crafting"));

				MinecraftRecipeSerializer.get().addMinecraftRecipe(gson.toJson(recipe), recipeType);
				MinecraftRecipeSerializer.get().sendSuccessMessage(recipeType, this.output.asItem().getRegistryName());
			}
		}
		catch(JsonIOException | IOException e)
		{
			CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
		}
	}
	
	private File getOutputFile()
	{
		File directory = new File(Minecraft.getInstance().gameDir, "Craft-Generator");
		if(!directory.exists())
			directory.mkdirs();

		return new File(directory, Objects.requireNonNull(output.asItem().getRegistryName()).getPath() + "_from_" + type.getType().getPath() + ".json");
	}
}
