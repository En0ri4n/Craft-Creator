package fr.eno.craftcreator.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.gui.ChatFormatting;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.container.CraftCreatorContainer;
import fr.eno.craftcreator.serializer.CraftingTableRecipeSerializer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CraftCreatorScreen extends ContainerScreen<CraftCreatorContainer>
{
	private Button shapedButton;
	private Button shapelessButton;
	private static final ResourceLocation CRAFT_CREATOR_TABLE_GUI_TEXTURES = new ResourceLocation(References.MOD_ID, "textures/gui/container/craft_creator.png");

	public CraftCreatorScreen(CraftCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected void init()
	{
		super.init();
		this.addButton(shapelessButton = new Button(guiLeft + 100, guiTop + 6, 68, 20, ChatFormatting.BOLD + "Shapeless", button ->
		{
			shapedButton.active = true;
			button.active = false;
		}));
		
		this.addButton(shapedButton = new Button(guiLeft + 100, guiTop + 60, 68, 20, ChatFormatting.BOLD + "Shaped", button ->
		{
			shapelessButton.active = true;
			button.active = false;
		}));
		
		this.addButton(new Button(guiLeft + 86, guiTop + 33, 30, 20, ChatFormatting.BOLD + "->", button ->
		{			
			if(!shapedButton.active)
			{
				Map<Integer, Item> map = new HashMap<Integer, Item>();
				int i = 0;
				for (int o = 1; o < 10; o++)
				{
					map.put(i, this.container.getInventory().get(o).getItem());
					i++;
				}
				
				CraftingTableRecipeSerializer.serializeShapedRecipe(this.container.getInventory().get(0), map);
			}
			else if(!shapelessButton.active)
			{
				List<Item> list = new ArrayList<Item>();
				
				for (int o = 1; o < 10; o++)
				{
					if(this.container.getInventory().get(o).getItem() != Items.AIR)
						list.add(this.container.getInventory().get(o).getItem());
				}
				
				CraftingTableRecipeSerializer.serializeShapelessRecipe(this.container.getInventory().get(0), list);
			}
		}));
		
		shapelessButton.active = false;
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