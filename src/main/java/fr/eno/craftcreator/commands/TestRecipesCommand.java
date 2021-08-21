package fr.eno.craftcreator.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import fr.eno.craftcreator.CraftCreator;
import fr.eno.craftcreator.utils.ReflectUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.storage.IServerConfiguration;

public class TestRecipesCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("testrecipes").requires((source) ->
		{
			return source.hasPermissionLevel(4);
		}).executes((ctx) ->
		{
			return execute(ctx);
		}));
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

		reloadPacks(ctx);

		ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.GREEN + "Recipes has been loaded successfully !"), false);

		return 1;
	}

	private static void reloadPacks(CommandContext<CommandSource> ctx)
	{
		CommandSource commandsource = ctx.getSource();
		MinecraftServer minecraftserver = commandsource.getServer();
		ResourcePackList resourcepacklist = minecraftserver.getResourcePacks();
		IServerConfiguration iserverconfiguration = minecraftserver.getServerConfiguration();
		Collection<String> collection = resourcepacklist.func_232621_d_();
		Collection<String> collection1 = getDatapackList(resourcepacklist, iserverconfiguration, collection);
		commandsource.sendFeedback(new TranslationTextComponent("commands.reload.success"), true);
		executeReload(collection1, commandsource);
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

	private static Collection<String> getDatapackList(ResourcePackList packList, IServerConfiguration serverConfig, Collection<String> p_241058_2_)
	{
		packList.reloadPacksFromFinders();
		Collection<String> collection = Lists.newArrayList(p_241058_2_);
		Collection<String> collection1 = serverConfig.getDatapackCodec().getDisabled();

		for (String s : packList.func_232616_b_())
		{
			if(!collection1.contains(s) && !collection.contains(s))
			{
				collection.add(s);
			}
		}

		return collection;
	}

	public static void executeReload(Collection<String> packs, CommandSource source)
	{
		source.getServer().func_240780_a_(packs).exceptionally((p_241061_1_) ->
		{
			CraftCreator.LOGGER.warn("Failed to execute reload", p_241061_1_);
			source.sendErrorMessage(new TranslationTextComponent("commands.reload.failure"));
			return null;
		});
	}
}