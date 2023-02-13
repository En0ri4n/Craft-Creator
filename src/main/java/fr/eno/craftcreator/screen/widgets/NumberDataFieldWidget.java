package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Collections;

public class NumberDataFieldWidget extends TextFieldWidget
{
    private ITextComponent tooltip;
    private boolean isDouble;

    public NumberDataFieldWidget(FontRenderer font, int x, int y, int width, int height, Number defaultValue, boolean isDouble)
    {
        super(font, x, y, width, height, new StringTextComponent(""));
        this.tooltip = new StringTextComponent("");
        this.setValue(String.valueOf(defaultValue));
        this.isDouble = isDouble;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint) || (codePoint == '.' && isDouble() && !getValue().contains("."))) return super.charTyped(codePoint, modifiers);

        return false;
    }

    @Override
    public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(!ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height) || !this.visible || !this.active || this.tooltip.getString().isEmpty())
            return;

        ClientUtils.getCurrentScreen().renderComponentTooltip(poseStack, Collections.singletonList(tooltip), mouseX, mouseY);
    }

    public void setNumberValue(Number number, boolean isDouble)
    {
        this.isDouble = isDouble;
        this.setValue(isDouble ? String.valueOf(number.doubleValue()) : String.valueOf(number.intValue()));
    }

    public double getDoubleValue()
    {
        return Double.parseDouble(this.getValue());
    }

    public int getIntValue()
    {
        return Integer.parseInt(this.getValue());
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
