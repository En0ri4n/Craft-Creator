package fr.eno.craftcreator.commands;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.eno.craftcreator.api.ServerUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.FolderName;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestRecipesCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("testrecipes").requires((source) -> source.hasPermission(4))
				.executes(TestRecipesCommand::execute));
	}

	public static int execute(CommandContext<CommandSource> ctx)
	{
		File recipeFolder = createDatapack(ctx);
		File generatorFolder = new File(ctx.getSource().getServer().getServerDirectory(), "Craft-Creator");

		File[] datapackRecipeFolder = recipeFolder.listFiles();

		if(datapackRecipeFolder != null)
			for (File f : datapackRecipeFolder)
				f.delete();

		for (File file : Utils.notNull(generatorFolder.listFiles()))
			try
			{
				FileUtils.copyFileToDirectory(file, recipeFolder, false);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

		ServerUtils.doCommand(ctx, "/reload");
		ctx.getSource().sendSuccess(new StringTextComponent(TextFormatting.GREEN + "Recipes has been loaded successfully !"), false);
		register(ctx.getSource().getServer().getCommands().getDispatcher());

		return 0;
	}

	private static File createDatapack(CommandContext<CommandSource> ctx)
	{
		File datapackFolder = ctx.getSource().getServer().getWorldPath(FolderName.DATAPACK_DIR).toFile();
		File packFolder = new File(datapackFolder, "Craft-Creator");
		packFolder.mkdirs();

		// Create pack.mcmeta file
		File packMCMeta = new File(packFolder, "pack.mcmeta");

		try
		{
			if(!packMCMeta.exists())
				packMCMeta.createNewFile();

			JsonObject obj = new JsonObject();
			JsonObject packObj = new JsonObject();
			packObj.addProperty("pack_format", 1);
			packObj.addProperty("description", "A basic datapack made by the mod Craft Creator - by En0ri4n");
			obj.add("pack", packObj);

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(packMCMeta)))
			{
				new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(obj, writer);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		File recipeFolder = new File(packFolder, "data\\craftcreator\\recipes");
		recipeFolder.mkdirs();

		return recipeFolder;
	}
}