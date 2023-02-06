package fr.eno.craftcreator.screen.widgets.buttons.pressable;

import net.minecraft.client.gui.widget.button.Button;

public class NullPressable implements Button.IPressable
{
	private static NullPressable INSTANCE;

	@Override
	public void onPress(Button button) {}

	public static NullPressable get()
	{
		return INSTANCE == null ? INSTANCE = new NullPressable() : INSTANCE;
	}
}