package fr.eno.craftcreator.screen;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.buttons.ExecuteButton;
import fr.eno.craftcreator.screen.buttons.MultipleItemChoiceButton;
import fr.eno.craftcreator.screen.buttons.SimpleCheckBox;
import fr.eno.craftcreator.utils.CraftType;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Map;

@SuppressWarnings("deprecation")
public class FurnaceRecipeCreatorScreen extends ContainerScreen<FurnaceRecipeCreatorContainer>
{
	private MultipleItemChoiceButton<Item, CraftType> recipeTypeButton;
	private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/furnace_recipe_creator.png");
	private final Map<Item, CraftType> RECIPE_TYPE_MAP = ImmutableMap.of(Items.FURNACE, CraftType.FURNACE_SMELTING, Items.BLAST_FURNACE, CraftType.FURNACE_BLASTING, Items.SMOKER, CraftType.FURNACE_SMOKING, Items.CAMPFIRE, CraftType.CAMPFIRE_COOKING);

	private SimpleCheckBox isKubeJSRecipeButton;
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
		this.addButton(recipeTypeButton = new MultipleItemChoiceButton<>(this.guiLeft + xSize - 20, guiTop, 20, 20, RECIPE_TYPE_MAP));


		this.addButton(isKubeJSRecipeButton = new SimpleCheckBox(5, this.height - 20, 15, 15, References.getTranslate("screen.recipe_creator_screen.kube_js_button"), false));
		this.addButton(new ExecuteButton(guiLeft + 76, guiTop + 33, 32, button -> MinecraftRecipeSerializer.createFurnaceRecipe(this.container.getInventory(), this.getRecipeType(), expField.getText(), cookTimeField.getText(), isKubeJSRecipeButton.isChecked())));
		
		expField = new TextFieldWidget(font, guiLeft + 8, guiTop + 30, 40, 10, new StringTextComponent(""));
		expField.setText("0.1");
		cookTimeField = new TextFieldWidget(font, guiLeft + 8, guiTop + 60, 40, 10, new StringTextComponent(""));
		cookTimeField.setText("200");

		if(!SupportedMods.isKubeJSLoaded())
			this.isKubeJSRecipeButton.visible = false;
	}
	
	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		double scale = 0.7D;
		RenderSystem.pushMatrix();
		RenderSystem.scaled(scale, scale, scale);
		String expStr = "Exp. Gained :";
		String cookTimeStr = "Cook Time :";
		font.drawString(matrixStack, expStr, (int) ((guiLeft + 8) / scale), (int) ((guiTop + 30 - font.FONT_HEIGHT * scale) / scale), Color.WHITE.getRGB());
		font.drawString(matrixStack, cookTimeStr, (int) ((guiLeft + 8) / scale), (int) ((guiTop + 60 - font.FONT_HEIGHT * scale) / scale), Color.WHITE.getRGB());
		RenderSystem.popMatrix();
		this.expField.render(matrixStack, mouseX, mouseY, partialTicks);
		this.cookTimeField.render(matrixStack, mouseX, mouseY, partialTicks);
		this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
		blit(matrixStack, this.guiLeft + 57, this.guiTop + 37, 176, 0, 14, 14, 256, 256);
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
	protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {}
}