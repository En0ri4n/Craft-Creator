package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.gui.ChatFormatting;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.StoneCutterRecipeCreatorContainer;
import fr.eno.craftcreator.utils.CraftHelper;
import fr.eno.craftcreator.utils.GuiUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
		
		this.addButton(new Button(guiLeft + 69, guiTop + 31, 30, 20, ChatFormatting.BOLD + "->", button -> CraftHelper.createStoneCutterRecipe(this.container.getInventory())));
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