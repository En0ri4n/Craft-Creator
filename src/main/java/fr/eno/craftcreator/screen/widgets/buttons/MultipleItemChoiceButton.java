package fr.eno.craftcreator.screen.widgets.buttons;


import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.ScreenUtils;
import fr.eno.craftcreator.screen.widgets.buttons.pressable.NullPressable;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class MultipleItemChoiceButton<K extends Item, V> extends Button
{
	private final Map<K, V> map;
	private int currentIndex;
	private V currentValue;
	private K currentKey;

	public MultipleItemChoiceButton(int x, int y, int width, int height, Map<K, V> map)
	{
		super(x, y, width, height, new TextComponent(""), new NullPressable());
		this.map = map;
		this.currentKey = new ArrayList<>(map.keySet()).get(0);
		this.currentValue = map.get(currentKey);
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			ScreenUtils.renderSizedButton(matrixStack, x, y, this.width, this.height, active, ScreenUtils.isMouseHover(x, y, mouseX, mouseY, width, height));
			
			ClientUtils.getItemRenderer().renderAndDecorateFakeItem(new ItemStack(currentKey), this.x + this.width / 2 - 8, this.y + this.height / 2 - 8);
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

		this.currentKey = new ArrayList<>(map.keySet()).get(currentIndex);
		this.currentValue = map.get(currentKey);
	}

	public V getCurrentValue()
	{
		return currentValue;
	}
}