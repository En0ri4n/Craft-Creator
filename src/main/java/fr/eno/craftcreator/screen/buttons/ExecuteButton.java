package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class ExecuteButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/arrow_button.png");

	public ExecuteButton(int x, int y, int width, IPressable onPress)
	{
		super(x, y, width, 20, new StringTextComponent(""), onPress);
	}

	@Override
	public void renderButton(MatrixStack pPoseStack, int mouseX, int mouseY, float partialTick)
	{
		if(this.visible)
		{
			int yOffset = 0;
			ClientUtils.bindTexture(TEXTURE);
			
			if(active)
			{
				yOffset = Utils.isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
			}

			Screen.blit(pPoseStack, x, y, this.width, this.height, 0, yOffset, 42, 20, 42, 40);
		}
	}
}
