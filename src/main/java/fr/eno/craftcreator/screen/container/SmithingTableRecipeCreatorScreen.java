package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.SmithingTableRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.List;

public class SmithingTableRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<SmithingTableRecipeCreatorContainer>
{
	private SimpleCheckBox isKubeJSRecipeButton;

	public SmithingTableRecipeCreatorScreen(SmithingTableRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
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

		setExecuteButtonPos(this.leftPos + this.imageWidth / 2, this.topPos + 35);
	}

	@Override
	protected List<ModRecipeCreator> getAvailableRecipesCreator()
	{
		return List.of(ModRecipeCreator.SMITHING_TABLE);
	}

	@Override
	protected RecipeInfos getRecipeInfos()
	{
		super.getRecipeInfos();
		recipeInfos.addParameter(new RecipeInfos.RecipeParameterBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE, isKubeJSRecipeButton.selected()));
		return recipeInfos;
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		int yTextureOffset = ExecuteButton.isMouseHover(this.leftPos + imageWidth - 20, topPos, mouseX, mouseY, 20, 20) ? 20 : 0;
		RenderSystem.setShaderTexture(0, References.getLoc("textures/gui/buttons/item_button.png"));
		RenderSystem.enableBlend();
		Screen.blit(matrixStack, this.leftPos + imageWidth - 20, topPos, 20, 20, 0, yTextureOffset, 20, 20, 20, 40);
	}

	@Override
	protected List<PositionnedSlot> getTaggeableSlots()
	{
		return SlotHelper.SMITHING_TABLE_SLOTS;
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
	}

	@Override
	protected Item getRecipeIcon()
	{
		return Items.SMITHING_TABLE;
	}

	@Override
	protected PairValues<Integer, Integer> getIconPos()
	{
		return PairValues.create(this.leftPos + imageWidth - 18, topPos + 2);
	}
}