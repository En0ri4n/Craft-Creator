package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

public class SimpleButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");
	private final Minecraft minecraft = Minecraft.getInstance();
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
	public void renderWidget(MatrixStack pPoseStack, int mouseX, int mouseY, float pPartialTick)
	{
		if(this.visible && this.active)
		{
			int yOffset = 0;
			ClientUtils.bindTexture(TEXTURE);
			
			if(active)
			{
				yOffset = ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width, height) ? 40 : 20;
			}

			Screen.blit(pPoseStack, x, y, this.width, this.height, 0, yOffset, 100, 20, 100, 60);
			drawCenteredString(pPoseStack, ClientUtils.getFont(), this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void renderToolTip(MatrixStack pPoseStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			ClientUtils.getCurrentScreen().func_243308_b(pPoseStack, tooltips, mouseX, mouseY);
	}
}