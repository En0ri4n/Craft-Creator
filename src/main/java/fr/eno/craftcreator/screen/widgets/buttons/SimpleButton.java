package fr.eno.craftcreator.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.screen.widgets.IOutsideWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class SimpleButton extends Button implements IOutsideWidget
{
	private final List<ITextComponent> tooltips;

	public SimpleButton(ITextComponent value, int x, int y, int width, int height, IPressable onPress)
	{
		this(value, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleButton(ITextComponent value, List<ITextComponent> tooltips, int x, int y, int width, int height, IPressable onPress)
	{
		super(x, y, width, height, value, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float pPartialTick)
	{
		if(this.visible && this.active)
		{
			ScreenUtils.renderSizedButton(matrixStack, x, y, width, height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));

			drawCenteredString(matrixStack, ClientUtils.getFontRenderer(), this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void renderToolTip(MatrixStack pPoseStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			ClientUtils.getCurrentScreen().renderComponentTooltip(pPoseStack, tooltips, mouseX, mouseY);
	}

	@Override
	public Rectangle2d getArea()
	{
		if(this.visible)
			return new Rectangle2d(x - 5, y - 5, width + 10, height + 10);
		else
			return new Rectangle2d(0, 0, 0, 0);
	}
}