package fr.eno.craftcreator.screen.utils;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ChildrenScreen extends ListScreen
{
    private final Screen parent;

    protected ChildrenScreen(Component titleIn, Screen parentIn)
    {
        super(titleIn);
        this.parent = parentIn;
    }

    public void leaveGUI()
    {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
