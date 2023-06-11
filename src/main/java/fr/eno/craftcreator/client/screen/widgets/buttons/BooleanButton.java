package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;


public class BooleanButton extends Button
{
	private boolean isOn;
	private final String name;

	public BooleanButton(String name, int x, int y, int width, int height, boolean value, IPressable onPress)
	{
		super(x, y, width, height, new StringTextComponent(""), onPress);
		this.name = name;
		this.isOn = value;
	}

	@Override
	public void renderButton(MatrixStack pPoseStack, int mouseX, int mouseY, float pPartialTick)
	{
		if(this.visible)
		{
			ScreenUtils.renderSizedButton(pPoseStack, x, y, this.width, this.height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));

			String str = References.getTranslate("button.boolean." + name + "." + (isOn ? "on" : "off")).getString();
			drawCenteredString(pPoseStack, ClientUtils.getFontRenderer(), str, this.x + this.width / 2, this.y + this.height / 3, 0xFFFFFF);
		}
	}

	@Override
	public void onClick(double p_onClick_1_, double p_onClick_3_)
	{
		this.isOn = !this.isOn;
		super.onClick(p_onClick_1_, p_onClick_3_);
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