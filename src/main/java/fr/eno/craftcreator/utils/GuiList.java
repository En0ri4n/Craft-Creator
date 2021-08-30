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
    private final int x;
    private final int y;
    private final int width;
    private final int keyHeight;

    private List<T> keys;
    private T selectedKey;

    public GuiList(Screen parent, int x, int y, int width, int keyHeight)
    {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.keyHeight = keyHeight;
    }

    public void render(int mouseX, int mouseY)
    {
        mc.getTextureManager().bindTexture(References.getLoc("textures/gui/container/gui_background.png"));
        Screen.blit(x, y, width, this.keys.size() * keyHeight + this.keys.size() + 6, 0, 0, 256, 256, 256, 256);

        for(int i = 0; i < this.keys.size(); i++)
        {
            mc.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/basic_button.png"));

            int x = this.x + 3;
            int y = this.y + 3 + i * keyHeight + i;
            int stringY = y + keyHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2;

            if(GuiUtils.isMouseHover(x, y, mouseX, mouseY, width, keyHeight))
                Screen.blit(x, y, this.width - 6, keyHeight, 0, 20, 100, 20, 100, 60);
            else
                Screen.blit(x, y, this.width - 6, keyHeight, 0, 40, 100, 20, 100, 60);

            if(this.getSelectedKey() != null && this.getSelectedKey() == this.keys.get(i))
                parent.drawCenteredString(mc.fontRenderer, this.keys.get(i).toString(), x + this.width / 2 - 3, stringY, Color.GREEN.getRGB());
            else
                parent.drawCenteredString(mc.fontRenderer, this.keys.get(i).toString(), x + this.width / 2 - 3, stringY, Color.WHITE.getRGB());
        }
    }

    public void mouseClicked(int mouseX, int mouseY, Callable<T> result)
    {
        if(this.getKeys() == null) return;

        for(int i = 0; i < this.keys.size(); i++)
        {
            int x = this.x + 3;
            int y = this.y + 3 + (i * keyHeight + (i > 0 ? 1 : 0));

            if(GuiUtils.isMouseHover(x, y, mouseX, mouseY, width - 6, keyHeight))
            {
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
