package fr.eno.craftcreator;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.commands.TestRecipesCommand;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.kubejs.KubeJSManager;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.container.*;
import fr.eno.craftcreator.utils.EntryHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        CommonUtils.setDefaultItemblockRender(InitBlocks.STONE_CUTTER_RECIPE_CREATOR.get());
        CommonUtils.setDefaultItemblockRender(InitBlocks.CRAFTING_TABLE_RECIPE_CREATOR.get());
        CommonUtils.setDefaultItemblockRender(InitBlocks.FURNACE_RECIPE_CREATOR.get());
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ClientRegistry.registerKeyBinding(ClientUtils.KEY_OPEN_RECIPES_MENU);

            ClientUtils.registerScreen(InitContainers.CRAFTING_TABLE_RECIPE_CREATOR.get(), CraftingTableRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.FURNACE_RECIPE_CREATOR.get(), FurnaceRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.STONE_CUTTER_RECIPE_CREATOR.get(), StoneCutterRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.SMITHING_TABLE_RECIPE_CREATOR.get(), SmithingTableRecipeCreatorScreen::new);

            if(SupportedMods.BOTANIA.isLoaded()) ClientUtils.registerScreen(InitContainers.BOTANIA_RECIPE_CREATOR.get(), BotaniaRecipeCreatorScreen::new);
            if(SupportedMods.THERMAL.isLoaded()) ClientUtils.registerScreen(InitContainers.THERMAL_RECIPE_CREATOR.get(), ThermalRecipeCreatorScreen::new);
        });
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartedEvent event)
    {
        event.getServer().getWorlds().forEach(EntryHelper::init);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e)
    {
        TestRecipesCommand.register(e.getDispatcher());
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