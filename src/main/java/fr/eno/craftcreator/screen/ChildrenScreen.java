package fr.eno.craftcreator.screen;

import net.minecraft.client.gui.screen.*;
import net.minecraft.util.text.*;

public class ChildrenScreen extends ListScreen
{
    private final Screen parent;

    protected ChildrenScreen(ITextComponent titleIn, Screen parentIn)
    {
        super(titleIn);
        this.parent = parentIn;
    }

    public void leaveGUI()
    {
        minecraft.displayGuiScreen(parent);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
