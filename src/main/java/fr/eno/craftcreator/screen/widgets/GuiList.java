package fr.eno.craftcreator.screen.widgets;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;
import java.util.function.Consumer;

public class GuiList<T> implements IOutsideWidget
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

    public void render(PoseStack matrixStack, int mouseX, int mouseY)
    {
        int width = (getMaxWidth() + 12);
        ClientUtils.bindTexture(BACKGROUND_TEXTURE);
        ScreenUtils.renderSizedTexture(matrixStack, 4, this.guiListRight - width - 6, y, width + 6, getHeight(), 0, 0, 16, 16, 16);

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            int x = this.guiListRight - width - 3;
            int y = this.y + 3 + i * keyHeight + i;
            int buttonTitle = y + keyHeight / 2 - ClientUtils.getFontRenderer().lineHeight / 2;

            ScreenUtils.renderSizedButton(matrixStack, x, y, width, keyHeight, true, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, keyHeight));
            //Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, xTexture, yTexture, widthToGet, heightToGet, textureWidth, textureHeight);

            if(i >= this.keys.size())
            {
                Screen.drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), References.getTranslate("screen.guiList.reset").getString(), x + width / 2 - 3, buttonTitle, 0xFFFFFF);
                continue;
            }

            boolean isSelected = this.selectedKey != null && this.selectedKey.toString().equals(this.keys.get(i).toString());

            if(isSelected)
                Screen.drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.keys.get(i).toString(), x + width / 2, buttonTitle, 0x0dc70d);
            else
                Screen.drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.keys.get(i).toString(), x + width / 2, buttonTitle, 0xFFFFFF);
        }
    }

    public int getX()
    {
        return this.guiListRight - (getMaxWidth() + 12) - 6;
    }

    public int getY()
    {
        return this.y;
    }

    public int getWidth()
    {
        return getMaxWidth() + 12;
    }

    public int getHeight()
    {
        return (this.keys.size() + 1) * keyHeight + (this.keys.size() + 1) + 6;
    }

    public int getMaxWidth()
    {
        if(this.keys == null) return 0;

        int max = ClientUtils.getFontRenderer().width(References.getTranslate("screen.guiList.reset").getString());
        for(T key : this.keys)
        {
            int width = ClientUtils.getFontRenderer().width(key.toString());

            if(width > max)
                max = width;
        }

        return max;
    }

    public void mouseClicked(int mouseX, int mouseY, Consumer<T> result)
    {
        if(this.getKeys() == null) return;

        int width = getMaxWidth();

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            int finalWidth = (width + 10);
            int x = this.guiListRight - finalWidth + 3;
            int y = this.y + 3 + i * keyHeight + i;

            if(ScreenUtils.isMouseHover(x, y, mouseX, mouseY, finalWidth - 6, keyHeight))
            {
                if(i >= this.keys.size())
                {
                    this.setSelectedKey(null);
                    result.accept(null);
                    ClientUtils.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.25F, SoundSource.MASTER, false);
                    continue;
                }

                this.setSelectedKey(this.keys.get(i));
                result.accept(this.getSelectedKey());
                ClientUtils.playSound(SoundEvents.UI_BUTTON_CLICK, 1F, 0.25F, SoundSource.MASTER, false);
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

    @Override
    public Rect2i getArea()
    {
        if(getKeys() != null)
            return new Rect2i(getX(), getY(), getWidth(), getHeight());
        else
            return new Rect2i(getX(), getY(), 0, 0);
    }
}
