package fr.eno.craftcreator.screen.widgets.buttons.pressable;


import net.minecraft.client.gui.components.Button;

public class NullPressable implements Button.OnPress
{
	private static NullPressable INSTANCE;

	@Override
	public void onPress(Button button) {}

	public static NullPressable get()
	{
		return INSTANCE == null ? INSTANCE = new NullPressable() : INSTANCE;
	}
}