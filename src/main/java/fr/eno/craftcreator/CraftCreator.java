package fr.eno.craftcreator;

import fr.eno.craftcreator.commands.*;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.kubejs.*;
import fr.eno.craftcreator.kubejs.utils.*;
import fr.eno.craftcreator.screen.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;

import javax.annotation.*;
import java.util.function.*;

@Mod(References.MOD_ID)
public class CraftCreator
{
	public static final Logger LOGGER = LogManager.getLogger();

	public CraftCreator()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		KubeJSManager.initialize();

		bus.addListener(this::setup);
		bus.addListener(this::clientSetup);

		MinecraftForge.EVENT_BUS.register(this);

		InitBlocks.BLOCKS.register(bus);
		InitContainers.CONTAINERS.register(bus);
		InitItems.ITEMS.register(bus);
		InitTileEntities.TILE_ENTITY.register(bus);

		InitPackets.initNetwork();
		InitPackets.registerMessages();
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		Predicate<RenderType> render = (r) -> r == RenderType.getCutout();
		
		RenderTypeLookup.setRenderLayer(InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get(), render);
		RenderTypeLookup.setRenderLayer(InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get(), render);
		RenderTypeLookup.setRenderLayer(InitBlocks.FURNACE_RECIPE_CREATOR.get(), render);
	}
	
	public void clientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), CraftingTableRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.FURNACE_RECIPE_CREATOR.get(), FurnaceRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), StoneCutterRecipeCreatorScreen::new);
		ScreenManager.registerFactory(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), SmithingTableRecipeCreatorScreen::new);

		if(SupportedMods.isBotaniaLoaded())
			ScreenManager.registerFactory(InitContainers.BOTANIA_RECIPE_CREATOR.get(), BotaniaRecipeCreatorScreen::new);
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent e)
	{
		TestRecipesCommand.register(e.getDispatcher());
	}
	
	public static final ItemGroup CRAFT_CREATOR_TAB = new ItemGroup(References.MOD_ID)
	{
		@Nonnull
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(InitItems.CRAFTING_TABLE_RECIPE_CREATOR.get());
		}
	};
}