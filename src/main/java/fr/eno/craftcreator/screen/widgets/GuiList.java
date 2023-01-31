package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class GuiList<T>
{
    private final int guiListRight;
    private final int y;
    private final int keyHeight;
    private List<T> keys;
    private T selectedKey;

    public GuiList(int guiListRight, int y, int keyHeight)
    {
        this.guiListRight = guiListRight;
        this.y = y;
        this.keyHeight = keyHeight;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        int width = getMaxWidth();
        ClientUtils.bindTexture(References.getLoc("textures/gui/container/gui_background.png"));
        Screen.blit(matrixStack, this.guiListRight - (width + 10), y, width + 10, (this.keys.size() + 1) * keyHeight + (this.keys.size() + 1) + 6, 0, 0, 256, 256, 256, 256);

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            ClientUtils.bindTexture(References.getLoc("textures/gui/buttons/basic_button.png"));

            int finalWidth = (width + 10);
            int x = this.guiListRight - finalWidth + 3;
            int y = this.y + 3 + i * keyHeight + i;
            int buttonTitle = y + keyHeight / 2 - ClientUtils.getFont().FONT_HEIGHT / 2;

            if(ExecuteButton.isMouseHover(x, y, mouseX, mouseY, finalWidth, keyHeight))
                Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, 0, 20, 100, 20, 100, 60);
            else
                Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, 0, 40, 100, 20, 100, 60);

            if(i >= this.keys.size())
            {
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), References.getTranslate("screen.guiList.reset").getString(), x + finalWidth / 2 - 3, buttonTitle, 0xFFFFFF);
                continue;
            }

            boolean isSelected = this.selectedKey != null && this.selectedKey.toString().equals(this.keys.get(i).toString());

            if(isSelected)
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), this.keys.get(i).toString(), x + finalWidth / 2 - 3, buttonTitle, 0x0dc70d);
            else
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), this.keys.get(i).toString(), x + finalWidth / 2 - 3, buttonTitle, 0xFFFFFF);
        }
    }

    private int getMaxWidth()
    {
        if(this.keys == null) return 0;

        int max = ClientUtils.getFont().getStringWidth(References.getTranslate("screen.guiList.reset").getString());
        for(T key : this.keys)
        {
            int width = ClientUtils.getFont().getStringWidth(key.toString());

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
            int finalWidth = (width + 10);
            int x = this.guiListRight - finalWidth + 3;
            int y = this.y + 3 + i * keyHeight + i;

            if(ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width - 6, keyHeight))
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
