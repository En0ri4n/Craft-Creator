package fr.eno.craftcreator.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.SmithingTableRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;

public class SmithingTableRecipeCreatorScreen extends AbstractContainerScreen<SmithingTableRecipeCreatorContainer>
{
	private static final ResourceLocation GUI_TEXTURES = References.getLoc("textures/gui/container/smithing_table_recipe_creator.png");
	private SimpleCheckBox isKubeJSRecipeButton;

	public SmithingTableRecipeCreatorScreen(SmithingTableRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();

		this.addRenderableWidget(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));

		this.addRenderableWidget(new ExecuteButton(leftPos + 98, topPos + 45, 30, button -> MinecraftRecipeSerializer.createSmithingTableRecipe(this.getMenu().getItems(), this.isKubeJSRecipeButton.selected())));

		if(!SupportedMods.isKubeJSLoaded())
			this.isKubeJSRecipeButton.visible = false;
	}
	
	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		minecraft.getTextureManager().bindForSetup(References.getLoc("textures/gui/buttons/item_button.png"));
		int yTextureOffset = ExecuteButton.isMouseHover(this.leftPos + imageWidth - 20, topPos, mouseX, mouseY, 20, 20) ? 20 : 0;
		Screen.blit(matrixStack, this.leftPos + imageWidth - 20, topPos, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
		minecraft.getItemRenderer().renderGuiItem(new ItemStack(Items.SMITHING_TABLE), this.leftPos + imageWidth - 18, topPos + 2);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindForSetup(GUI_TEXTURES);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) {}
}