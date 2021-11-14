package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class SimpleButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");
	private final Minecraft minecraft = Minecraft.getInstance();
	private final List<ITextProperties> tooltips;

	public SimpleButton(TextComponent value, int x, int y, int width, int height, IPressable onPress)
	{
		this(value, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleButton(TextComponent value, List<ITextProperties> tooltips, int x, int y, int width, int height, IPressable onPress)
	{
		super(x, y, width, height, value, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible && this.active)
		{
			int yOffset = 0;
			mc.getTextureManager().bindTexture(TEXTURE);
			
			if(active)
			{
				yOffset = ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width, height) ? 40 : 20;
			}

			Screen.blit(matrixStack, x, y, this.width, this.height, 0, yOffset, 100, 20, 100, 60);
			drawCenteredString(matrixStack, mc.fontRenderer, this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, Color.WHITE.getRGB());
		}
	}

	public void renderTooltips(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight(), -1, minecraft.fontRenderer);
	}
}