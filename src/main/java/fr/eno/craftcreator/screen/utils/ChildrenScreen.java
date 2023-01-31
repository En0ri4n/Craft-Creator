package fr.eno.craftcreator.screen.utils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class ChildrenScreen extends ListScreen
{
    private final Screen parent;

    protected ChildrenScreen(ITextComponent titleIn, Screen parentIn)
    {
        super(titleIn);
        this.parent = parentIn;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
