package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class ExecuteButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/arrow_button.png");

	public ExecuteButton(int x, int y, int width, IPressable onPress)
	{
		super(x, y, width, 20, new StringTextComponent(""), onPress);
	}

	@Override
	public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible)
		{
			int yOffset = 0;
			mc.getTextureManager().bindTexture(TEXTURE);
			
			if(active)
			{
				yOffset = isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
			}

			Screen.blit(matrixStack, x, y, this.width, this.height, 0, yOffset, 42, 20, 42, 40);
		}
	}

	public static boolean isMouseHover(int x, int y, int mouseX, int mouseY, int width, int height)
	{
		return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
	}
}
