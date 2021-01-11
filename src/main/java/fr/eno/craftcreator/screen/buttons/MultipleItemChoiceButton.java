package fr.eno.craftcreator.screen.buttons;

import java.util.Map;
import java.util.stream.Collectors;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.screen.buttons.pressable.NullPressable;
import fr.eno.craftcreator.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class MultipleItemChoiceButton<K extends Item, V> extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/item_button.png");
	Minecraft mc = Minecraft.getInstance();
	private final Map<K, V> map;
	private int currentIndex;
	private V currentValue;
	private K currentKey;

	public MultipleItemChoiceButton(int x, int y, int width, int height, Map<K, V> map)
	{
		super(x, y, width, height, "", new NullPressable());
		this.map = map;
		this.currentKey = map.keySet().stream().collect(Collectors.toList()).get(0);
		this.currentValue = map.get(currentKey);
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			mc.getTextureManager().bindTexture(TEXTURE);			
			int yOffset = GuiUtils.isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
			Screen.blit(x, y, this.width, this.height, 0, yOffset, 20, 20, 20, 40);
			
			mc.getItemRenderer().renderItemIntoGUI(new ItemStack(currentKey), this.x + this.width / 2 - 8, this.y + this.height / 2 - 8);
		}
	}

	@Override
	public void onClick(double p_onClick_1_, double p_onClick_3_)
	{
		currentIndex++;
		checkIndex();
	}
	
	private void checkIndex()
	{		
		if(map.size() <= currentIndex)
		{
			currentIndex = 0;
		}

		this.currentKey = map.keySet().stream().collect(Collectors.toList()).get(currentIndex);
		this.currentValue = map.get(currentKey);
	}

	public int getCurrentIndex()
	{
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex)
	{
		this.currentIndex = currentIndex;
		checkIndex();
	}

	public V getCurrentValue()
	{
		return currentValue;
	}

	public K getCurrentKey()
	{
		return currentKey;
	}
}