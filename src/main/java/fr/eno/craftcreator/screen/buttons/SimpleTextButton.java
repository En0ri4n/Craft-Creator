package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SimpleTextButton extends Button
{
	private final Minecraft minecraft = Minecraft.getInstance();
	private final List<Component> tooltips;

	public SimpleTextButton(Component content, int x, int y, int width, int height, net.minecraft.client.gui.components.Button.OnPress onPress)
	{
		this(content, Collections.emptyList(), x, y, width, height, onPress);
	}

	public SimpleTextButton(Component content, List<Component> tooltips, int x, int y, int width, int height, net.minecraft.client.gui.components.Button.OnPress onPress)
	{
		super(x, y, width, height, content, onPress);
		this.tooltips = tooltips;
	}

	@Override
	public void renderButton(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible && this.active)
		{
			Screen.fill(matrixStack, x, y, x + width, y + height, 0x777879);
			Screen.fill(matrixStack, x + 1, y + 1, x + width - 1, y + height - 1, 0x000000);
			drawCenteredString(matrixStack, mc.font, this.getMessage().getString(), this.x + this.width / 2, this.y + height / 2 - mc.font.lineHeight / 2 + 1, 0xFFFFFF);
		}
	}

	public void setContent(Component content)
	{
		this.setMessage(content);
	}

	@Override
	public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty() && minecraft.screen != null)
			minecraft.screen.renderTooltip(matrixStack, tooltips, Optional.empty(), mouseX, mouseY);
	}
}