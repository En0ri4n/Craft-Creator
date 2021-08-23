package fr.eno.craftcreator;

import java.util.function.Predicate;

import fr.eno.craftcreator.commands.*;
import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.event.server.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eno.craftcreator.init.InitBlocks;
import fr.eno.craftcreator.init.InitContainers;
import fr.eno.craftcreator.init.InitItems;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.screen.CraftingTableRecipeCreatorScreen;
import fr.eno.craftcreator.screen.FurnaceRecipeCreatorScreen;
import fr.eno.craftcreator.screen.SmithingTableRecipeCreatorScreen;
import fr.eno.craftcreator.screen.StoneCutterRecipeCreatorScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(References.MOD_ID)
public class CraftCreator
{
	public static final Logger LOGGER = LogManager.getLogger();

	public CraftCreator()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::setup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::onServerStart);

		MinecraftForge.EVENT_BUS.register(this);

		InitBlocks.BLOCKS.register(bus);
		InitContainers.CONTAINERS.register(bus);
		InitItems.ITEMS.register(bus);
		InitTileEntities.TILE_ENTITY.register(bus);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		Predicate<RenderType> render = (r) -> r == RenderType.getCutout();
		
		RenderTypeLookup.setRenderLayer(InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get(), render);
	}
	
	public void clientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), CraftingTableRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.FURNACE_RECIPE_CREATOR.get(), FurnaceRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), StoneCutterRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), SmithingTableRecipeCreatorScreen::new);
	}

	@SubscribeEvent
	public void onServerStart(FMLServerStartingEvent e)
	{
		TestRecipesCommand.register(e.getCommandDispatcher());
	}
	
	public static final ItemGroup CRAFT_CREATOR_TAB = new ItemGroup(References.MOD_ID)
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(InitItems.CRAFTING_TABLE_RECIPE_CREATOR.get());
		}
	};
}