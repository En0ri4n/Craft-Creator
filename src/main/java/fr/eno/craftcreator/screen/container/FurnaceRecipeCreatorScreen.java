package fr.eno.craftcreator.screen.container;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.MultipleItemChoiceButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.Map;

@SuppressWarnings("deprecation")
public class FurnaceRecipeCreatorScreen extends AbstractContainerScreen<FurnaceRecipeCreatorContainer>
{
	private MultipleItemChoiceButton<Item, CraftType> recipeTypeButton;
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/furnace_recipe_creator.png");
	private final Map<Item, CraftType> RECIPE_TYPE_MAP = ImmutableMap.of(Items.FURNACE, CraftType.FURNACE_SMELTING, Items.BLAST_FURNACE, CraftType.FURNACE_BLASTING, Items.SMOKER, CraftType.FURNACE_SMOKING, Items.CAMPFIRE, CraftType.CAMPFIRE_COOKING);

	private SimpleCheckBox isKubeJSRecipeButton;
	private EditBox expField;
	private EditBox cookTimeField;
	
	public FurnaceRecipeCreatorScreen(FurnaceRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();
		this.addRenderableWidget(recipeTypeButton = new MultipleItemChoiceButton<>(this.leftPos + imageWidth - 20, topPos, 20, 20, RECIPE_TYPE_MAP));


		this.addRenderableWidget(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
		this.addRenderableWidget(new ExecuteButton(leftPos + 76, topPos + 33, 32, button -> MinecraftRecipeSerializer.createFurnaceRecipe(this.getMenu().getItems(), this.getRecipeType(), expField.getValue(), cookTimeField.getValue(), isKubeJSRecipeButton.selected())));
		
		expField = new EditBox(font, leftPos + 8, topPos + 30, 40, 10, new TextComponent(""));
		expField.setValue("0.1");
		cookTimeField = new EditBox(font, leftPos + 8, topPos + 60, 40, 10, new TextComponent(""));
		cookTimeField.setValue("200");

		if(!SupportedMods.isKubeJSLoaded())
			this.isKubeJSRecipeButton.visible = false;
	}
	
	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		float scale = 0.7F;
		matrixStack.pushPose();
		matrixStack.scale(scale, scale, scale);
		String expStr = "Exp. Gained :";
		String cookTimeStr = "Cook Time :";
		font.drawShadow(matrixStack, expStr, (int) ((leftPos + 8) / scale), (int) ((topPos + 30 - font.lineHeight * scale) / scale), 0xFFFFFF);
		font.drawShadow(matrixStack, cookTimeStr, (int) ((leftPos + 8) / scale), (int) ((topPos + 60 - font.lineHeight * scale) / scale), 0xFFFFFF);
		matrixStack.popPose();
		this.expField.render(matrixStack, mouseX, mouseY, partialTicks);
		this.cookTimeField.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderTexture(0, GUI_TEXTURES);
		blit(matrixStack, this.leftPos + 57, this.topPos + 37, 176, 0, 14, 14, 256, 256);
	}

	@Override
	protected void renderBg(@Nonnull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI_TEXTURES);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
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

	@Override
	protected void renderLabels(@Nonnull PoseStack matrixStack, int x, int y) {}
}