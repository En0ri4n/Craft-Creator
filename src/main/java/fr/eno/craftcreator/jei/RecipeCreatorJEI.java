package fr.eno.craftcreator.jei;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.container.BotaniaRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.MinecraftRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.ThermalRecipeCreatorScreen;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class RecipeCreatorJEI implements IModPlugin
{
    public RecipeCreatorJEI() {}

    @Override
    public ResourceLocation getPluginUid()
    {
        return References.getLoc("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        IGuiContainerHandler<MultiScreenModRecipeCreatorScreen<?>> guiContainerHandler = new MultiScreenModRecipeCreatorScreenGuiHandler<>();
        registration.addGuiContainerHandler(MinecraftRecipeCreatorScreen.class, guiContainerHandler);
        registration.addGuiContainerHandler(BotaniaRecipeCreatorScreen.class, guiContainerHandler);
        registration.addGuiContainerHandler(ThermalRecipeCreatorScreen.class, guiContainerHandler);
    }
}
