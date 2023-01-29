package fr.eno.craftcreator.screen.container;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.eno.craftcreator.container.StoneCutterRecipeCreatorContainer;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class StoneCutterRecipeCreatorScreen extends MultiScreenModRecipeCreatorScreen<StoneCutterRecipeCreatorContainer>
{
	public StoneCutterRecipeCreatorScreen(StoneCutterRecipeCreatorContainer screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn, screenContainer.getTile().getBlockPos());
		isVanillaScreen = true;
	}

	@Override
	protected void updateScreen()
	{
		super.updateScreen();

		setExecuteButtonPos(this.leftPos + this.imageWidth / 2 - 26, this.topPos + 31);
	}

	@Override
	public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected List<ModRecipeCreator> getAvailableRecipesCreator()
	{
		return List.of(ModRecipeCreator.STONECUTTER);
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
	protected List<PositionnedSlot> getTaggeableSlots()
	{
		return new ArrayList<>();
	}
}