package fr.eno.craftcreator;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.commands.TestRecipesCommand;
import fr.eno.craftcreator.init.*;
import fr.eno.craftcreator.recipes.kubejs.KubeJSManager;
import fr.eno.craftcreator.screen.container.BotaniaRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.CreateRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.MinecraftRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.ThermalRecipeCreatorScreen;
import fr.eno.craftcreator.utils.ClientDispatcher;
import fr.eno.craftcreator.utils.EntryHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

        bus.addListener(this::clientSetup);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, ClientDispatcher::get);

        MinecraftForge.EVENT_BUS.register(this);

        InitBlocks.BLOCKS.register(bus);
        InitContainers.CONTAINERS.register(bus);
        InitItems.ITEMS.register(bus);
        InitTileEntities.TILE_ENTITIES.register(bus);

        InitPackets.initNetwork();
        KubeJSManager.initialize();
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ClientRegistry.registerKeyBinding(ClientUtils.KEY_OPEN_RECIPES_MENU);
            ClientRegistry.registerKeyBinding(ClientUtils.KEY_OPEN_TUTORIAL);

            ClientUtils.setDefaultBlockRender(InitBlocks.BOTANIA_RECIPE_CREATOR.get());
            ClientUtils.setDefaultBlockRender(InitBlocks.MINECRAFT_RECIPE_CREATOR.get());
            ClientUtils.setDefaultBlockRender(InitBlocks.THERMAL_RECIPE_CREATOR.get());
            ClientUtils.setDefaultBlockRender(InitBlocks.CREATE_RECIPE_CREATOR.get());

            ClientUtils.registerScreen(InitContainers.MINECRAFT_RECIPE_CREATOR.get(), MinecraftRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.BOTANIA_RECIPE_CREATOR.get(), BotaniaRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.THERMAL_RECIPE_CREATOR.get(), ThermalRecipeCreatorScreen::new);
            ClientUtils.registerScreen(InitContainers.CREATE_RECIPE_CREATOR.get(), CreateRecipeCreatorScreen::new);
        });
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartedEvent event)
    {
        event.getServer().getAllLevels().forEach(EntryHelper::init);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e)
    {
        TestRecipesCommand.register(e.getDispatcher());
    }

    public static final ItemGroup CRAFT_CREATOR_TAB = new ItemGroup(References.MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(InitItems.MINECRAFT_RECIPE_CREATOR.get());
        }
    };
}