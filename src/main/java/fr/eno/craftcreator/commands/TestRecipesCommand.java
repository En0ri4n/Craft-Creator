package fr.eno.craftcreator.commands;

import com.google.gson.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.context.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.command.*;
import net.minecraft.util.text.*;
import net.minecraft.world.server.*;
import org.apache.commons.io.*;

import java.io.*;
import java.lang.reflect.*;

public class TestRecipesCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("testrecipes").requires((source) -> source.hasPermissionLevel(4))
				.executes(TestRecipesCommand::execute));
	}

	public static int execute(CommandContext<CommandSource> ctx)
	{
		File recipeFolder = createDatapack(ctx);
		File generatorFolder = new File(ctx.getSource().getServer().getDataDirectory(), "Craft-Generator");

		File[] datapackRecipeFolder = recipeFolder.listFiles();

		if(datapackRecipeFolder != null)
			for (File f : datapackRecipeFolder)
				f.delete();

		for (File file : generatorFolder.listFiles())
			try
			{
				FileUtils.copyFileToDirectory(file, recipeFolder);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

		ctx.getSource().getServer().getCommandManager().handleCommand(ctx.getSource(), "/reload");
		ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Recipes has been loaded successfully !"), false);
		register(ctx.getSource().getServer().getFunctionManager().getCommandDispatcher());

		return 0;
	}

	private static File createDatapack(CommandContext<CommandSource> ctx)
	{
		Field worldDirField = ReflectUtils.getFieldAndSetAccessible(ChunkManager.class, "field_219270_x");
		File worldDir = ReflectUtils.getFieldValue(worldDirField, ctx.getSource().getWorld().getChunkProvider().chunkManager, File.class);

		File datapackFolder = new File(worldDir, "datapacks");
		File packFolder = new File(datapackFolder, "Craft-Creator");
		packFolder.mkdirs();
		File packMCMeta = new File(packFolder, "pack.mcmeta");

		try
		{
			if(!packMCMeta.exists())
				packMCMeta.createNewFile();

			JsonObject obj = new JsonObject();
			JsonObject packObj = new JsonObject();
			packObj.addProperty("pack_format", 1);
			packObj.addProperty("description", "A basic datapack made by the mod Craft Creator - by Eno_gamer10");
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

		File dataFolder = new File(packFolder, "data");
		dataFolder.mkdirs();
		File craftCreatorDataFolder = new File(dataFolder, "craft_creator");
		craftCreatorDataFolder.mkdirs();
		File recipeFolder = new File(craftCreatorDataFolder, "recipes");
		recipeFolder.mkdirs();

		return recipeFolder;
	}
}