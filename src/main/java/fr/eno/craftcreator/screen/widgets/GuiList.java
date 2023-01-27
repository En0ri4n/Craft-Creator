package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.utils.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class GuiList<T>
{
    final Minecraft mc = Minecraft.getInstance();

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
        int width = getMaxWidth();
        RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/container/gui_background.png"));
        Screen.blit(matrixStack, this.guiListRight - (width + 10), y, width + 10, (this.keys.size() + 1) * keyHeight + (this.keys.size() + 1) + 6, 0, 0, 256, 256, 256, 256);

        for(int i = 0; i < this.keys.size() + 1; i++)
        {
            RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/buttons/basic_button.png"));

            int finalWidth = (width + 10);
            int x = this.guiListRight - finalWidth + 3;
            int y = this.y + 3 + i * keyHeight + i;
            int buttonTitle = y + keyHeight / 2 - mc.font.lineHeight / 2;

            if(ExecuteButton.isMouseHover(x, y, mouseX, mouseY, finalWidth, keyHeight))
                Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, 0, 20, 100, 20, 100, 60);
            else
                Screen.blit(matrixStack, x, y, finalWidth - 6, keyHeight, 0, 40, 100, 20, 100, 60);

            if(i >= this.keys.size())
            {
                GuiComponent.drawCenteredString(matrixStack, mc.font, References.getTranslate("screen.guiList.reset").getString(), x + finalWidth / 2 - 3, buttonTitle, 0xFFFFFF);
                continue;
            }

            boolean isSelected = this.selectedKey != null && this.selectedKey.toString().equals(this.keys.get(i).toString());

            if(isSelected)
                GuiComponent.drawCenteredString(matrixStack, mc.font, this.keys.get(i).toString(), x + finalWidth / 2 - 3, buttonTitle, 0x0dc70d);
            else
                GuiComponent.drawCenteredString(matrixStack, mc.font, this.keys.get(i).toString(), x + finalWidth / 2 - 3, buttonTitle, 0xFFFFFF);
        }
    }

    private int getMaxWidth()
    {
        if(this.keys == null) return 0;

        int max = mc.font.width(References.getTranslate("screen.guiList.reset").getString());
        for(T key : this.keys)
        {
            int width = mc.font.width(key.toString());

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
