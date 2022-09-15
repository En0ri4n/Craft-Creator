package fr.eno.craftcreator.commands;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.eno.craftcreator.utils.ReflectUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ChunkMap;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class TestRecipesCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("testrecipes").requires((source) -> source.hasPermission(4))
				.executes(TestRecipesCommand::execute));
	}

	public static int execute(CommandContext<CommandSourceStack> ctx)
	{
		File recipeFolder = createDatapack(ctx);
		File generatorFolder = new File(ctx.getSource().getServer().getServerDirectory(), "Craft-Generator");

		File[] datapackRecipeFolder = recipeFolder.listFiles();

		if(datapackRecipeFolder != null)
			for (File f : datapackRecipeFolder)
				f.delete();

		for (File file : Objects.requireNonNull(generatorFolder.listFiles()))
			try
			{
				FileUtils.copyFileToDirectory(file, recipeFolder);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

		ctx.getSource().getServer().getCommands().performCommand(ctx.getSource(), "/reload");
		ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.GREEN + "Recipes has been loaded successfully !"), false);
		register(ctx.getSource().getServer().getFunctions().getDispatcher());

		return 0;
	}

	private static File createDatapack(CommandContext<CommandSourceStack> ctx)
	{
		Field worldDirField = ReflectUtils.getFieldAndSetAccessible(ChunkMap.class, "f_182284_");
		File worldDir = new File(Objects.requireNonNull(ReflectUtils.getFieldValue(worldDirField, ctx.getSource().getLevel().getChunkSource().chunkMap, String.class)));

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