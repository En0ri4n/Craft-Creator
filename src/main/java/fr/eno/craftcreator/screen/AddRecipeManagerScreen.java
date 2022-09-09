package fr.eno.craftcreator.screen;

import net.minecraft.util.text.StringTextComponent;

public class AddRecipeManagerScreen extends ChildrenScreen
{
    public AddRecipeManagerScreen(RecipeManagerSelectionScreen parent)
    {
        super(new StringTextComponent(""), parent);
    }
}
