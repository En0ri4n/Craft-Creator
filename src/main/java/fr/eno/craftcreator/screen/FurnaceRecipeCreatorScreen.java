package fr.eno.craftcreator.screen;

import com.google.common.collect.*;
import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

import java.awt.*;
import java.util.*;

public class FurnaceRecipeCreatorScreen extends ContainerScreen<FurnaceRecipeCreatorContainer>
{
	private MultipleItemChoiceButton<Item, CraftType> recipeTypeButton;
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/furnace_recipe_creator.png");
	private final Map<Item, CraftType> RECIPE_TYPE_MAP = ImmutableMap.of(Items.FURNACE, CraftType.FURNACE_SMELTING, Items.BLAST_FURNACE, CraftType.FURNACE_BLASTING, Items.SMOKER, CraftType.FURNACE_SMOKING, Items.CAMPFIRE, CraftType.CAMPFIRE_COOKING);
	
	private TextFieldWidget expField;
	private TextFieldWidget cookTimeField;
	
	public FurnaceRecipeCreatorScreen(FurnaceRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();
		this.addButton(recipeTypeButton = new MultipleItemChoiceButton<Item, CraftType>(this.guiLeft + xSize - 20, guiTop, 20, 20, RECIPE_TYPE_MAP));

		this.addButton(new ExecuteButton(guiLeft + 76, guiTop + 33, 32, button -> CraftHelper.createFurnaceRecipe(this.container.getInventory(), this.getRecipeType(), expField.getText(), cookTimeField.getText())));
		
		expField = new TextFieldWidget(font, guiLeft + 8, guiTop + 30, 40, 10, "");
		expField.setText("0.1");
		cookTimeField = new TextFieldWidget(font, guiLeft + 8, guiTop + 60, 40, 10, "");
		cookTimeField.setText("200");
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{		
		super.render(mouseX, mouseY, partialTicks);

		double scale = 0.7D;
		RenderSystem.pushMatrix();
		RenderSystem.scaled(scale, scale, scale);
		String expStr = "Exp. Gained :";
		String cookTimeStr = "Cook Time :";
		font.drawString(expStr, (int) ((guiLeft + 8) / scale), (int) ((guiTop + 30 - font.FONT_HEIGHT * scale) / scale), Color.WHITE.getRGB());
		font.drawString(cookTimeStr, (int) ((guiLeft + 8) / scale), (int) ((guiTop + 60 - font.FONT_HEIGHT * scale) / scale), Color.WHITE.getRGB());
		RenderSystem.popMatrix();
		this.expField.render(mouseX, mouseY, partialTicks);
		this.cookTimeField.render(mouseX, mouseY, partialTicks);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
		blit(this.guiLeft + 57, this.guiTop + 37, 176, 0, 14, 14, 256, 256);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public boolean charTyped(char code, int p_charTyped_2_)
	{
		if(Character.isDigit(code) || code == '.')
		{
			this.expField.charTyped(code, p_charTyped_2_);
			this.cookTimeField.charTyped(code, p_charTyped_2_);
		}
		
		return super.charTyped(code, p_charTyped_2_);
	}
	
	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_)
	{
		this.expField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		this.cookTimeField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
		
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
	{
		this.expField.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		this.cookTimeField.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	
	private CraftType getRecipeType()
	{
		return recipeTypeButton.getCurrentValue();
	}
}