package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.FurnaceRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class FurnaceRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	public FurnaceRecipeCreatorTile(BlockPos pWorldPosition, BlockState pBlockState)
	{
		super(InitTileEntities.FURNACE_RECIPE_CREATOR.get(), pWorldPosition, pBlockState, SlotHelper.FURNACE_SLOTS_SIZE);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player)
	{
		return new FurnaceRecipeCreatorContainer(id, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
	}

	@Nonnull
	@Override
	public Component getDisplayName()
	{
		return new TextComponent("container.furnace_recipe_creator.title");
	}
}