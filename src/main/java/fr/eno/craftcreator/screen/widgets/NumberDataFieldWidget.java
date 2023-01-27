package fr.eno.craftcreator.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.utils.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NumberDataFieldWidget extends EditBox
{
    private Component tooltip;

    public NumberDataFieldWidget(Font font, int x, int y, int width, int height, Number defaultValue)
    {
        super(font, x, y, width, height, new TextComponent(""));
        this.tooltip = new TextComponent("");
        this.setValue(String.valueOf(defaultValue));
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if(Character.isDigit(codePoint) || codePoint == '.') return super.charTyped(codePoint, modifiers);

        return false;
    }

    @Override
    public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY)
    {
        if(!ClientUtils.isMouseHover(x, y, mouseX, mouseY, width, height) || !this.visible || !this.active || this.tooltip.getString().isEmpty())
            return;

        List<Component> tooltips = new ArrayList<>();
        tooltips.add(new TextComponent("Value :").withStyle(ChatFormatting.GRAY));
        tooltips.add(this.tooltip.copy().withStyle(ChatFormatting.DARK_GRAY));
        ClientUtils.getCurrentScreen().renderTooltip(poseStack, tooltips, Optional.empty(), mouseX, mouseY);
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

    public void setTooltip(Component tooltip)
    {
        this.tooltip = tooltip;
    }
}
