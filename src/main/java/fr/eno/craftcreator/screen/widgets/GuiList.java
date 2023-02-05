package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.utils.Callable;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiList<T>
{
    private static final ResourceLocation BACKGROUND_TEXTURE = References.getLoc("textures/gui/container/gui_background.png");

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
        int width = (getMaxWidth() + 12);
        ClientUtils.bindTexture(BACKGROUND_TEXTURE);
        Utils.renderSizedTexture(matrixStack, 4, this.guiListRight - width - 6, y, width + 6, (this.keys.size() + 1) * keyHeight + (this.keys.size() + 1) + 6, 0, 0, 16, 16, 16);

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            int x = this.guiListRight - width - 3;
            int y = this.y + 3 + i * keyHeight + i;
            int buttonTitle = y + keyHeight / 2 - ClientUtils.getFont().lineHeight / 2;

            Utils.renderSizedButton(matrixStack, x, y, width, keyHeight, true, Utils.isMouseHover(x, y, mouseX, mouseY, width, keyHeight));
            //Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, xTexture, yTexture, widthToGet, heightToGet, textureWidth, textureHeight);

            if(i >= this.keys.size())
            {
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), References.getTranslate("screen.guiList.reset").getString(), x + width / 2 - 3, buttonTitle, 0xFFFFFF);
                continue;
            }

            boolean isSelected = this.selectedKey != null && this.selectedKey.toString().equals(this.keys.get(i).toString());

            if(isSelected)
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), this.keys.get(i).toString(), x + width / 2 - 3, buttonTitle, 0x0dc70d);
            else
                Screen.drawCenteredString(matrixStack, ClientUtils.getFont(), this.keys.get(i).toString(), x + width / 2 - 3, buttonTitle, 0xFFFFFF);
        }
    }

    private int getMaxWidth()
    {
        if(this.keys == null) return 0;

        int max = ClientUtils.getFont().width(References.getTranslate("screen.guiList.reset").getString());
        for(T key : this.keys)
        {
            int width = ClientUtils.getFont().width(key.toString());

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

            if(Utils.isMouseHover(x, y, mouseX, mouseY, width - 6, keyHeight))
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
