package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.gui.ChatFormatting;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.screen.buttons.BooleanButton;
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

public class CraftingTableRecipeCreatorScreen extends ContainerScreen<CraftingTableRecipeCreatorContainer>
{
	private BooleanButton craftTypeButton;
	private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/crafting_table_recipe_creator.png");

	public CraftingTableRecipeCreatorScreen(CraftingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();
		this.addButton(craftTypeButton = new BooleanButton("craftType", guiLeft + 100, guiTop + 60, 68, 20, true));

		this.addButton(new Button(guiLeft + 86, guiTop + 33, 30, 20, ChatFormatting.BOLD + "->", button -> CraftHelper.createCraftingTableRecipe(this.container.getInventory(), this.isShaped())));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);
		minecraft.getTextureManager().bindTexture(References.getLoc("textures/gui/buttons/item_button.png"));
		int yTextureOffset = GuiUtils.isMouseHover(this.guiLeft + xSize - 20, guiTop, mouseX, mouseY, 20, 20) ? 20 : 0;
		Screen.blit(this.guiLeft + xSize - 20, guiTop, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
		minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(Items.CRAFTING_TABLE), this.guiLeft + xSize - 18, guiTop + 2);
	}
	
	private boolean isShaped()
	{
		return craftTypeButton.isOn();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CRAFT_CREATOR_TABLE_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = (this.height - this.ySize) / 2;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
	}
}