package fr.eno.craftcreator.screen.widgets.buttons;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

public class BooleanButton extends Button
{
	private boolean isOn;
	private final String name;

	public BooleanButton(String name, int x, int y, int width, int height, boolean value, Button.OnPress onPress)
	{
		super(x, y, width, height, new TextComponent(""), onPress);
		this.name = name;
		this.isOn = value;
	}

	@Override
	public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float pPartialTick)
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