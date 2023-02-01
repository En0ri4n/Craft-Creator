package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class NumberDataFieldWidget extends TextFieldWidget
{
    private ITextComponent tooltip;

    public NumberDataFieldWidget(FontRenderer font, int x, int y, int width, int height, Number defaultValue)
    {
        super(font, x, y, width, height, new StringTextComponent(""));
        this.tooltip = new StringTextComponent("");
        this.setValue(String.valueOf(defaultValue));
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint) || codePoint == '.') return super.charTyped(codePoint, modifiers);

        return false;
    }

    @Override
    public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY)
    {
        if(!ClientUtils.isMouseHover(x, y, mouseX, mouseY, width, height) || !this.visible || !this.active || this.tooltip.getString().isEmpty())
            return;

        List<ITextComponent> tooltips = new ArrayList<>();
        tooltips.add(new StringTextComponent("Value :").withStyle(TextFormatting.GRAY));
        tooltips.add(this.tooltip.copy().withStyle(TextFormatting.DARK_GRAY));
        ClientUtils.getCurrentScreen().renderComponentTooltip(poseStack, tooltips, mouseX, mouseY);
    }

    public void setNumberValue(Number number)
    {
        this.setValue(String.valueOf(number));
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
}
