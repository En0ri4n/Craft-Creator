package fr.eno.craftcreator.jei;

import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;

import java.util.List;

public class MultiScreenModRecipeCreatorScreenGuiHandler implements IGuiContainerHandler<MultiScreenModRecipeCreatorScreen<?>>
{
    public MultiScreenModRecipeCreatorScreenGuiHandler() {}

    @Override
    public List<Rectangle2d> getGuiExtraAreas(MultiScreenModRecipeCreatorScreen<?> containerScreen)
    {
        return containerScreen.getExtraAreas();
    }
}
