package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.api.ClientUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class SimpleTextButton extends Button
{
	private final List<ITextComponent> tooltips;

	public SimpleTextButton(ITextComponent content, int x, int y, int width, int height, IPressable onPress)
	{
		this(content, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleTextButton(ITextComponent content, List<ITextComponent> tooltips, int x, int y, int width, int height, IPressable onPress)
	{
		super(x, y, width, height, content, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible && this.active)
		{
			boolean isHovered = ClientUtils.isMouseHover(this.x, this.y, mouseX, mouseY, this.width, this.height);
			int color = isHovered ? 0x00FF0050 : 0xFFFFFFFF;
			Screen.fill(matrixStack, x, y, x + width, y + height, 0x777879FF);
			Screen.fill(matrixStack, x + 1, y + 1, x + width - 1, y + height - 1, 0x00000000);
			drawCenteredString(matrixStack, ClientUtils.getFont(), this.getMessage().getString(), this.x + this.width / 2, this.y + height / 2 - ClientUtils.getFont().lineHeight / 2 + 1, color);
		}
	}

	public void setContent(ITextComponent content)
	{
		this.setMessage(content);
	}

	@Override
	public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty() && ClientUtils.getCurrentScreen() != null)
			ClientUtils.getCurrentScreen().renderComponentTooltip(matrixStack, tooltips, mouseX, mouseY);
	}
}