package fr.eno.craftcreator.jei;

import fr.eno.craftcreator.container.base.CommonContainer;
import fr.eno.craftcreator.screen.container.base.MultiScreenModRecipeCreatorScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.renderer.Rectangle2d;

import java.util.List;

public class MultiScreenModRecipeCreatorScreenGuiHandler<T extends MultiScreenModRecipeCreatorScreen<? extends CommonContainer>> implements IGuiContainerHandler<T>
{
    public MultiScreenModRecipeCreatorScreenGuiHandler() {
    }

    @Override
    public List<Rectangle2d> getGuiExtraAreas(T containerScreen)
    {
        return containerScreen.getExtraAreas();
    }
}
