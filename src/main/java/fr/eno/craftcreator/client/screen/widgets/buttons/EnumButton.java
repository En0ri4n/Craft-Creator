package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;
import java.util.Locale;

public class EnumButton<T> extends Button
{
    private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/gui_button.png");

    private List<T> values;
    private int color;
    private int index;

    public EnumButton(List<T> values, int pX, int pY, int pWidth, int pHeight, int color, IPressable pOnPress)
    {
        super(pX, pY, pWidth, pHeight, new StringTextComponent(""), pOnPress);
        this.values = values;
        this.color = color;
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        if(visible)
        {
            ClientUtils.bindTexture(TEXTURE);
            ScreenUtils.renderSizedButton(pMatrixStack, x, y, width, height, active, isMouseOver(pMouseX, pMouseY));
            Screen.drawCenteredString(pMatrixStack, ClientUtils.getFontRenderer(), getSelected().toString().toLowerCase(Locale.ROOT), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFFFFFFF);
        }
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if(clicked(pMouseX, pMouseY) && pButton == 0)
        {
            increaseIndex();
            onPress.onPress(this);
            playDownSound(ClientUtils.getMinecraft().getSoundManager());
            return true;
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
