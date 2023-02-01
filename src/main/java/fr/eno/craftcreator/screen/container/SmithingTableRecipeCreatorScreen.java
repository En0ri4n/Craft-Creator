package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.eno.craftcreator.container.SmithingTableRecipeCreatorContainer;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class SmithingTableRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<SmithingTableRecipeCreatorContainer>
{
	public SmithingTableRecipeCreatorScreen(SmithingTableRecipeCreatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
		isVanillaScreen = true;
	}

	@Override
	protected void updateScreen()
	{
		super.updateScreen();
		setExecuteButtonPos(this.leftPos + this.imageWidth / 2 + 7, this.topPos + 45);
		executeButton.setWidth(36);
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected List<ModRecipeCreator> getAvailableRecipesCreator()
	{
		return Collections.singletonList(ModRecipeCreator.SMITHING_TABLE);
	}

	@Override
	protected List<PositionnedSlot> getTaggeableSlots()
	{
		return SlotHelper.SMITHING_TABLE_SLOTS;
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