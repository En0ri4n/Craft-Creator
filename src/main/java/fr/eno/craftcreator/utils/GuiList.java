package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screen.*;

import java.awt.*;
import java.util.List;

public class GuiList<T>
{
    final Minecraft mc = Minecraft.getInstance();

    private final Screen parent;
    private final int guiListRight;
    private final int y;
    private final int keyHeight;
    private List<T> keys;
    private T selectedKey;

    public GuiList(Screen parent, int guiListRight, int y, int keyHeight)
    {
        this.parent = parent;
        this.guiListRight = guiListRight;
        this.y = y;
        this.keyHeight = keyHeight;
    }

    public void render(int mouseX, int mouseY)
    {
        int width = getMaxWidth();
        mc.getTextureManager().bindTexture(References.getLoc("textures/gui/container/gui_background.png"));
        Screen.blit(this.guiListRight - (width + 10), y, width + 10, (this.keys.size() + 1) * keyHeight + (this.keys.size() + 1) + 6, 0, 0, 256, 256, 256, 256);

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            mc.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/basic_button.png"));

            int finalWidth = (width + 10);
            int x = this.guiListRight - finalWidth + 3;
            int y = this.y + 3 + i * keyHeight + i;
            int stringY = y + keyHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2;

            if(GuiUtils.isMouseHover(x, y, mouseX, mouseY, finalWidth, keyHeight))
                Screen.blit(x, y, finalWidth - 6, keyHeight, 0, 20, 100, 20, 100, 60);
            else
                Screen.blit(x, y, finalWidth - 6, keyHeight, 0, 40, 100, 20, 100, 60);

            if(i >= this.keys.size())
            {
                parent.drawCenteredString(mc.fontRenderer, References.getTranslate("screen.guiList.reset").getString(), x + finalWidth / 2 - 3, stringY, Color.WHITE.getRGB());
            }
            else if(this.getSelectedKey() != null && this.getSelectedKey() == this.keys.get(i))
                parent.drawCenteredString(mc.fontRenderer, this.keys.get(i).toString(), x + finalWidth / 2 - 3, stringY, Color.GREEN.getRGB());
            else
                parent.drawCenteredString(mc.fontRenderer, this.keys.get(i).toString(), x + finalWidth / 2 - 3, stringY, Color.WHITE.getRGB());
        }
    }

    private int getMaxWidth()
    {
        if(this.keys == null) return 0;

        int max = mc.fontRenderer.getStringWidth(References.getTranslate("screen.guiList.reset").getString());
        for(T key : this.keys)
        {
            int width = mc.fontRenderer.getStringWidth(key.toString());

            if(width > max)
                max = width;
        }

        return max + 6;
    }

    public void mouseClicked(int mouseX, int mouseY, Callable<T> result)
    {
        if(this.getKeys() == null) return;

        int width = getMaxWidth();

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            int x = this.guiListRight - width + 3;
            int y = this.y + 3 + (i * keyHeight + (i > 0 ? 1 : 0));

            if(GuiUtils.isMouseHover(x, y, mouseX, mouseY, width - 6, keyHeight))
            {
                if(i >= this.keys.size())
                {
                    this.setSelectedKey(null);
                    result.run(null);
                    continue;
                }

                this.setSelectedKey(this.keys.get(i));
                result.run(this.getSelectedKey());
            }
        }
    }

    public List<T> getKeys()
    {
        return keys;
    }

    public void setKeys(List<T> keys)
    {
        this.keys = keys;
    }

    public T getSelectedKey()
    {
        return selectedKey;
    }

    public void setSelectedKey(T selectedKey)
    {
        this.selectedKey = selectedKey;
    }
}
