package fr.eno.craftcreator.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;

public class NumberDataFieldWidget extends TextFieldWidget
{
    private ITextComponent tooltip;
    private boolean isDouble;

    public NumberDataFieldWidget(int x, int y, int width, int height, Number defaultValue, boolean isDouble)
    {
        this(x, y, width, height, new StringTextComponent(""), defaultValue, isDouble);
    }

    public NumberDataFieldWidget(int x, int y, int width, int height, ITextComponent title, Number defaultValue, boolean isDouble)
    {
        super(ClientUtils.getFontRenderer(), x, y, width, height, title);
        this.tooltip = new StringTextComponent("");
        this.setValue(String.valueOf(defaultValue));
        this.isDouble = isDouble;
    }

    @Override
    public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        super.renderButton(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

        Screen.drawString(pMatrixStack, ClientUtils.getFontRenderer(), getMessage(), x - 3 - ClientUtils.width(getMessage().getString()), y + height / 2 - ClientUtils.getFontRenderer().lineHeight / 2, 0xffffffff);
    }

    @Override
    public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(!ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height) || !this.visible || !this.active || this.tooltip.getString().isEmpty())
            return;

        ClientUtils.getCurrentScreen().renderComponentTooltip(poseStack, Collections.singletonList(tooltip), mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint) || (codePoint == '.' && isDouble() && !getValue().contains(".")))
            return super.charTyped(codePoint, modifiers);

        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if(!isMouseOver(pMouseX, pMouseY) && getValue().trim().isEmpty())
            setValue("1");

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
    {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    public void setNumberValue(Number number, boolean isDouble)
    {
        this.isDouble = isDouble;
        this.setValue(isDouble ? String.valueOf(number.doubleValue()) : String.valueOf(number.intValue()));
    }

    public double getDoubleValue()
    {
        try
        {
            return Double.parseDouble(this.getValue());
        }
        catch(NumberFormatException e)
        {
            return 0D;
        }
    }

    public int getIntValue()
    {
        try
        {
            return Integer.parseInt(this.getValue());
        }
        catch(NumberFormatException e)
        {
            return 0;
        }
    }

    public void setTooltip(ITextComponent tooltip)
    {
        this.tooltip = tooltip;
    }

    public boolean isDouble()
    {
        return isDouble;
    }
}
