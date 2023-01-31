package fr.eno.craftcreator.screen;

import fr.eno.craftcreator.screen.utils.ChildrenScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class AddRecipeManagerScreen extends ChildrenScreen
{
    public AddRecipeManagerScreen(Screen parent)
    {
        super(new StringTextComponent(""), parent);
    }
}
