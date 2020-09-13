package fr.eno.craftcreator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eno.craftcreator.blocks.CraftCreatorBlock;
import fr.eno.craftcreator.container.CraftCreatorContainer;
import fr.eno.craftcreator.item.ItemBlockBasic;
import fr.eno.craftcreator.screen.CraftCreatorScreen;
import fr.eno.craftcreator.tileentity.CraftCreatorTile;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(References.MOD_ID)
public class CraftCreator
{
	public static final Logger LOGGER = LogManager.getLogger();

	public CraftCreator()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::setup);
		bus.addListener(this::clientSetup);

		ModRegistry.BLOCKS.register(bus);
		ModRegistry.CONTAINERS.register(bus);
		ModRegistry.ITEMS.register(bus);
		ModRegistry.TILE_ENTITY.register(bus);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		
	}
	
	public void clientSetup(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(ModRegistry.CRAFT_CREATOR_CONTAINER.get(), CraftCreatorScreen::new);
	}
	
	public static class ModRegistry
	{
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MOD_ID);
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MOD_ID);
		public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MOD_ID);
		public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, References.MOD_ID);

		public static final RegistryObject<Block> CRAFT_CREATOR_BLOCK = BLOCKS.register("craft_creator", () -> new CraftCreatorBlock());
		public static final RegistryObject<Item> CRAFT_CREATOR_ITEM = ITEMS.register("craft_creator", () -> new ItemBlockBasic(CRAFT_CREATOR_BLOCK.get()));
		public static final RegistryObject<ContainerType<CraftCreatorContainer>> CRAFT_CREATOR_CONTAINER = CONTAINERS.register("craft_creator", () -> IForgeContainerType.create(CraftCreatorContainer::new));
		public static final RegistryObject<TileEntityType<?>> CRAFT_CREATOR_TILE = TILE_ENTITY.register("craft_creator", () -> TileEntityType.Builder.create(CraftCreatorTile::new, CRAFT_CREATOR_BLOCK.get()).build(null));
	}
	
	public static final ItemGroup CRAFT_CREATOR_TAB = new ItemGroup(References.MOD_ID)
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModRegistry.CRAFT_CREATOR_ITEM.get());
		}
	};
}