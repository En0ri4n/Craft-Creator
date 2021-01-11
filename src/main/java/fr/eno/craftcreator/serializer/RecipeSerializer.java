package fr.eno.craftcreator.serializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.utils.CraftType;
import fr.eno.craftcreator.utils.Utilities;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IItemProvider;

public abstract class RecipeSerializer
{
	private static Minecraft mc = Minecraft.getInstance();
	private static final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

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

	private final void setType(CraftType type)
	{
		recipe.addProperty("type", type.toString());
	}
	
	public void serializeRecipe()
	{		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.getOutputFile())))
		{
			gson.toJson(recipe, writer);
			mc.player.sendMessage(Utilities.createClickableComponent(Utils.get("serializer.success", this.getOutputFile().getName()), this.getOutputFile()));
		}
		catch(JsonIOException | IOException e)
		{
			CraftCreator.LOGGER.error("Can't serialize the recipe in a json file !");
		}
	}
	
	private final File getOutputFile()
	{
		File directory = new File(Minecraft.getInstance().gameDir, "Craft-Generator");
		if(!directory.exists())
			directory.mkdirs();
		File recipeFile = new File(directory, output.asItem().getRegistryName().getPath() + "_from_" + type.getType().getPath() + ".json");

		return recipeFile;
	}
}
