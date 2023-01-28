package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.StoneCutterRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class StoneCutterRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<StoneCutterRecipeCreatorContainer>
{
	private SimpleCheckBox isKubeJSRecipeButton;
	
	public StoneCutterRecipeCreatorScreen(StoneCutterRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
	}

	@Override
	protected void init()
	{
		super.init();

		this.addRenderableWidget(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));

		if(!SupportedMods.isKubeJSLoaded())
			this.isKubeJSRecipeButton.visible = false;
	}

	@Override
	protected void updateScreen()
	{
		super.updateScreen();

		setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 26, this.topPos + 31);
	}

	@Override
	protected RecipeInfos getRecipeInfos()
	{
		super.getRecipeInfos();
		recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE, isKubeJSRecipeButton.selected()));
		return recipeInfos;
	}

	@Override
	protected List<ModRecipeCreator> getAvailableRecipesCreator()
	{
		return List.of(ModRecipeCreator.STONECUTTER);
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/buttons/item_button.png"));
		int yTextureOffset = ExecuteButton.isMouseHover(this.leftPos + imageWidth - 20, topPos, mouseX, mouseY, 20, 20) ? 20 : 0;
		RenderSystem.enableBlend();
		Screen.blit(matrixStack, this.leftPos + imageWidth - 20, topPos, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
		minecraft.getItemRenderer().renderGuiItem(new ItemStack(Items.STONECUTTER), this.leftPos + imageWidth - 18, topPos + 1);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
	}

	@Override
	protected Item getRecipeIcon()
	{
		return Items.STONECUTTER;
	}

	@Override
	protected PairValues<Integer, Integer> getIconPos()
	{
		return PairValues.create(this.leftPos + imageWidth - 18, topPos + 2);
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

	@Override
	protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) { super.renderLabels(matrixStack, x ,y); }

	@Override
	protected List<PositionnedSlot> getTaggeableSlots()
	{
		return new ArrayList<>();
	}
}