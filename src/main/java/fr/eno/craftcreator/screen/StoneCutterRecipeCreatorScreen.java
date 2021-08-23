package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.*;
import fr.eno.craftcreator.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.screen.buttons.*;
import fr.eno.craftcreator.utils.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

public class StoneCutterRecipeCreatorScreen extends ContainerScreen<StoneCutterRecipeCreatorContainer>
{
	private static final ResourceLocation GUI_TEXTURES = References.getLoc("textures/gui/container/stone_cutter_recipe_creator.png");
	
	public StoneCutterRecipeCreatorScreen(StoneCutterRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();
		
		this.addButton(new ExecuteButton(guiLeft + 69, guiTop + 31, 30, button -> CraftHelper.createStoneCutterRecipe(this.container.getInventory())));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);
		minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
		int yTextureOffset = GuiUtils.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
		Screen.blit(this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
		minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.STONECUTTER), this.guiLeft + xSize - 18, guiTop + 1);
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
		return super.charTyped(code, p_charTyped_2_);
	}
	
	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_)
	{		
		return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
	{
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
}