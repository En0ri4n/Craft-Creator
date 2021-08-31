package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.matrix.*;
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

import javax.annotation.*;
import java.awt.Color;

public class SmithingTableRecipeCreatorScreen extends ContainerScreen<SmithingTableRecipeCreatorContainer>
{
	private static final ResourceLocation GUI_TEXTURES = References.getLoc("textures/gui/container/smithing_table_recipe_creator.png");

	public SmithingTableRecipeCreatorScreen(SmithingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();

		this.addButton(new ExecuteButton(guiLeft + 98, guiTop + 45, 30, button -> CraftHelper.createSmithingTableRecipe(this.container.getInventory())));
	}
	
	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
		int yTextureOffset = ExecuteButton.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
		Screen.blit(matrixStack, this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
		minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.SMITHING_TABLE), this.guiLeft + xSize - 18, guiTop + 2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {}
}