package fr.eno.craftcreator.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class SimpleButton extends Button
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
			Utils.renderSizedButton(matrixStack, x, y, width, height, active, Utils.isMouseHover(x, y, mouseX, mouseY, width, height));

			drawCenteredString(matrixStack, ClientUtils.getFont(), this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void renderToolTip(MatrixStack pPoseStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			ClientUtils.getCurrentScreen().renderComponentTooltip(pPoseStack, tooltips, mouseX, mouseY);
	}
}