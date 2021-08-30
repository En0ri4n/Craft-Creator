package fr.eno.craftcreator.screen.buttons;

import java.awt.Color;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.pressable.NullPressable;
import fr.eno.craftcreator.utils.GuiUtils;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class BooleanButton extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/basic_button.png");

	private boolean isOn;
	private final String name;

	public BooleanButton(String name, int x, int y, int width, int height, boolean value, IPressable onPress)
	{
		super(x, y, width, height, "", onPress);
		this.name = name;
		this.isOn = value;
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float partialTicks)
	{
		Minecraft mc = Minecraft.getInstance();

		if(this.visible)
		{
			int yOffset = 0;
			mc.getTextureManager().bindTexture(TEXTURE);
			
			if(active)
			{
				yOffset = GuiUtils.isMouseHover(x, y, mouseX, mouseY, width, height) ? 40 : 20;
			}

			String str = Utils.getS("button.boolean." + name + "." + (isOn ? "on" : "off"));

			Screen.blit(x, y, this.width, this.height, 0, yOffset, 100, 20, 100, 60);
			drawCenteredString(mc.fontRenderer, str, this.x + this.width / 2, this.y + this.height / 3, Color.WHITE.getRGB());
		}
	}

	@Override
	public void onClick(double p_onClick_1_, double p_onClick_3_)
	{
		this.isOn = !this.isOn;
	}

	public boolean isOn()
	{
		return isOn;
	}

	public void setOn(boolean flag)
	{
		isOn = flag;
	}
}
