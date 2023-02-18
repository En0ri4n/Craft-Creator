package fr.eno.craftcreator.screen.widgets.buttons;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.screen.widgets.IOutsideWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public class SimpleButton extends Button implements IOutsideWidget
{
	private final List<Component> tooltips;

	public SimpleButton(Component value, int x, int y, int width, int height, OnPress onPress)
	{
		this(value, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleButton(Component value, List<Component> tooltips, int x, int y, int width, int height, OnPress onPress)
	{
		super(x, y, width, height, value, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float pPartialTick)
	{
		if(this.visible && this.active)
		{
			ScreenUtils.renderSizedButton(matrixStack, x, y, width, height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));

			drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void renderToolTip(PoseStack pPoseStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			ClientUtils.getCurrentScreen().renderComponentTooltip(pPoseStack, tooltips, mouseX, mouseY);
	}

	@Override
	public Rect2i getArea()
	{
		if(this.visible)
			return new Rect2i(x - 5, y - 5, width + 10, height + 10);
		else
			return new Rect2i(0, 0, 0, 0);
	}
}