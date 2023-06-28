package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.utils.Translatable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class EnumButton<T extends Translatable> extends Button
{
    private List<T> values;
    private int index;

    public EnumButton(List<T> values, int pX, int pY, int pWidth, int pHeight, ITextComponent pMessage, IPressable pOnPress)
    {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
        this.values = values;
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        if(visible)
        {
            Screen.fill(pMatrixStack, x, y, x + width, y + height, 0x88000000);
            Screen.drawCenteredString(pMatrixStack, ClientUtils.getFontRenderer(), getSelected().getTranslationComponent().getString(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if(clicked(pMouseX, pMouseY) && pButton == 0)
        {
            increaseIndex();
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }

        return false;
    }

    public T getSelected()
    {
        return values.get(index);
    }

    public void increaseIndex()
    {
        index++;
        if(index >= values.size())
            index = 0;
    }

    public void setSelected(T selected)
    {
        if(values.contains(selected))
        {
            this.index = values.indexOf(selected);
        }
    }

    public void setItems(List<T> values)
    {
        this.values = values;
        setSelected(this.values.get(0));
    }
}
