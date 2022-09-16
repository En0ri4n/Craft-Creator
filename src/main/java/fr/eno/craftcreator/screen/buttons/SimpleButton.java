package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SimpleButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");
	private final Minecraft minecraft = Minecraft.getInstance();
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
	public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float pPartialTick)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible && this.active)
		{
			int yOffset = 0;
			RenderSystem.setShaderTexture(0, TEXTURE);
			
			if(active)
			{
				yOffset = ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width, height) ? 40 : 20;
			}

			Screen.blit(pPoseStack, x, y, this.width, this.height, 0, yOffset, 100, 20, 100, 60);
			drawCenteredString(pPoseStack, mc.font, this.getMessage().getString(), this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void renderToolTip(PoseStack pPoseStack, int mouseX, int mouseY)
	{
		if(!this.tooltips.isEmpty())
			this.minecraft.screen.renderTooltip(pPoseStack, tooltips, Optional.empty(), mouseX, mouseY);
	}
}