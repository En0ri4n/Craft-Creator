package fr.eno.craftcreator.screen.widgets;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.Collections;

public class NumberDataFieldWidget extends EditBox
{
    private Component tooltip;
    private boolean isDouble;

    public NumberDataFieldWidget(Font font, int x, int y, int width, int height, Number defaultValue, boolean isDouble)
    {
        super(font, x, y, width, height, new TextComponent(""));
        this.tooltip = new TextComponent("");
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
    public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY)
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

    public void setTooltip(Component tooltip)
    {
        this.tooltip = tooltip;
    }

    public boolean isDouble()
    {
        return isDouble;
    }
}
