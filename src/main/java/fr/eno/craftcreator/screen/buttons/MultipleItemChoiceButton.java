package fr.eno.craftcreator.screen.buttons;

import com.mojang.blaze3d.matrix.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.screen.buttons.pressable.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

import javax.annotation.*;
import java.util.*;

public class MultipleItemChoiceButton<K extends Item, V> extends Button
{
	private static final ResourceLocation TEXTURE = References.getLoc("textures/gui/buttons/item_button.png");
	final Minecraft mc = Minecraft.getInstance();
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
	public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		if(this.visible)
		{
			mc.getTextureManager().bindTexture(TEXTURE);			
			int yOffset = ExecuteButton.isMouseHover(x, y, mouseX, mouseY, width, height) ? 20 : 0;
			Screen.blit(matrixStack, x, y, this.width, this.height, 0, yOffset, 20, 20, 20, 40);
			
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

		this.currentKey = new ArrayList<>(map.keySet()).get(currentIndex);
		this.currentValue = map.get(currentKey);
	}

	public V getCurrentValue()
	{
		return currentValue;
	}
}