package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.gui.ScrollPanel;

public class SimpleScrollPanel extends ScrollPanel
{
    public SimpleScrollPanel(Minecraft client, int y, int x, int width, int height)
    {
        super(client, width, height, y, x);
    }

    @Override
    protected int getContentHeight()
    {
        return 10;
    }

    @Override
    protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY)
    {

    }
}
