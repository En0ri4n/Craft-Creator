package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class SimpleTextButton extends Button
{
	private final Minecraft minecraft = Minecraft.getInstance();
	private final List<ITextProperties> tooltips;

	public SimpleTextButton(TextComponent content, int x, int y, int width, int height, IPressable onPress)
	{
		this(content, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleTextButton(TextComponent content, List<ITextProperties> tooltips, int x, int y, int width, int height, IPressable onPress)
	{
		super(x, y, width, height, content, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible && this.active)
		{
			Screen.fill(matrixStack, x, y, x + width, y + height, Color.GRAY.getRGB());
			Screen.fill(matrixStack, x + 1, y + 1, x + width - 1, y + height - 1, Color.BLACK.getRGB());
			drawCenteredString(matrixStack, mc.fontRenderer, this.getMessage().getString(), this.x + this.width / 2, this.y + height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1, Color.WHITE.getRGB());
		}
	}

	public void setContent(TextComponent content)
	{
		this.setMessage(content);
	}

	public void renderTooltips(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
	}
}