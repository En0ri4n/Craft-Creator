package fr.eno.craftcreator.jei;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.container.MinecraftRecipeCreatorScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        System.out.println("Registering JEI Gui Handler");
        registration.addGuiContainerHandler(MinecraftRecipeCreatorScreen.class, new MinecraftGuiHandler());
    }

    static class MinecraftGuiHandler implements IGuiContainerHandler<MinecraftRecipeCreatorScreen>
    {
        public MinecraftGuiHandler() {}

        @Override
        public List<Rectangle2d> getGuiExtraAreas(MinecraftRecipeCreatorScreen containerScreen)
        {
            return containerScreen.getExtraAreas();
        }

        @Override
        public Collection<IGuiClickableArea> getGuiClickableAreas(MinecraftRecipeCreatorScreen containerScreen, double mouseX, double mouseY)
        {
            return Collections.emptyList();
        }

        @Nullable
        @Override
        public Object getIngredientUnderMouse(MinecraftRecipeCreatorScreen containerScreen, double mouseX, double mouseY)
        {
            return null;
        }
    }
}
