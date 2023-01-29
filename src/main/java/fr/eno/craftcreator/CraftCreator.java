package fr.eno.craftcreator;

import fr.eno.craftcreator.commands.TestRecipesCommand;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.kubejs.KubeJSManager;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.container.*;
import fr.eno.craftcreator.utils.ClientUtils;
import fr.eno.craftcreator.utils.EntryHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

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
        Predicate<RenderType> render = (r) -> r == RenderType.cutout();

        ItemBlockRenderTypes.setRenderLayer(InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get(), render);
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get(), render);
        ItemBlockRenderTypes.setRenderLayer(InitBlocks.FURNACE_RECIPE_CREATOR.get(), render);
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ClientRegistry.registerKeyBinding(ClientUtils.KEY_OPEN_RECIPES_MENU);

            MenuScreens.register(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), CraftingTableRecipeCreatorScreen::new);
            MenuScreens.register(InitContainers.FURNACE_RECIPE_CREATOR.get(), FurnaceRecipeCreatorScreen::new);
            MenuScreens.register(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), StoneCutterRecipeCreatorScreen::new);
            MenuScreens.register(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), SmithingTableRecipeCreatorScreen::new);

            if(SupportedMods.BOTANIA.isLoaded()) MenuScreens.register(InitContainers.BOTANIA_RECIPE_CREATOR.get(), BotaniaRecipeCreatorScreen::new);
            if(SupportedMods.THERMAL.isLoaded()) MenuScreens.register(InitContainers.THERMAL_RECIPE_CREATOR.get(), ThermalRecipeCreatorScreen::new);
        });
    }

    @SubscribeEvent
    public void onServerStart(ServerStartedEvent event)
    {
        event.getServer().getAllLevels().forEach(EntryHelper::init);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e)
    {
        TestRecipesCommand.register(e.getDispatcher());
    }

    public static final CreativeModeTab CRAFT_CREATOR_TAB = new CreativeModeTab(References.MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(InitItems.CRAFTING_TABLE_RECIPE_CREATOR.get());
        }
    };
}