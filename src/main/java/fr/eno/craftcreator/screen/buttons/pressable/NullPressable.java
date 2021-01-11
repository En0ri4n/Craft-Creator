package fr.eno.craftcreator.screen.buttons.pressable;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;

public class NullPressable implements IPressable
{
	@Override
	public void onPress(Button button) {}
}