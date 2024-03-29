package fr.eno.craftcreator.client.screen.widgets.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.client.utils.ScreenUtils;
import fr.eno.craftcreator.client.screen.widgets.buttons.pressable.NullPressable;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

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
		super(x, y, width, height, new StringTextComponent(""), new NullPressable());
		this.map = map;
		this.currentKey = new ArrayList<>(map.keySet()).get(0);
		this.currentValue = map.get(currentKey);
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
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