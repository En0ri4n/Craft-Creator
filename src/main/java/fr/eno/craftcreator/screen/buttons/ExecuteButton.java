package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class ExecuteButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/arrow_button.png");

	public ExecuteButton(int x, int y, int width, OnPress onPress)
	{
		super(x, y, width, 20, new TextComponent(""), onPress);
	}

	@Override
	public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float partialTick)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible)
		{
			int yOffset = 0;
			RenderSystem.setShaderTexture(0, TEXTURE);
			
			if(active)
			{
				yOffset = isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
			}

			Screen.blit(pPoseStack, x, y, this.width, this.height, 0, yOffset, 42, 20, 42, 40);
		}
	}

	public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
	{
		return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
	}
}
