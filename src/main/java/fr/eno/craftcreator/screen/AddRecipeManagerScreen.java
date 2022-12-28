package fr.eno.craftcreator.screen;

import fr.eno.craftcreator.screen.utils.ChildrenScreen;
import net.minecraft.network.chat.TextComponent;

public class AddRecipeManagerScreen extends ChildrenScreen
{
    public AddRecipeManagerScreen(RecipeManagerSelectionScreen parent)
    {
        super(new TextComponent(""), parent);
    }
}
