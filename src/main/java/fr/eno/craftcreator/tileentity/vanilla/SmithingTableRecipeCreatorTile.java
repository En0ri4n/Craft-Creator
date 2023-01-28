package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.SmithingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.MultiScreenRecipeCreatorTile;
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

public class SmithingTableRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	public SmithingTableRecipeCreatorTile(BlockPos pWorldPosition, BlockState pBlockState)
	{
		super(InitTileEntities.SMITHING_TABLE_RECIPE_CREATOR.get(), pWorldPosition, pBlockState, SlotHelper.SMITHING_TABLE_SLOTS_SIZE);
	}

	@Nonnull
	@Override
	public Component getDisplayName()
	{
		return new TextComponent("container.smithing_table_recipe_creator.title");
	}

	@Override
	public AbstractContainerMenu createMenu(int id, @Nonnull Inventory playerInventory, @Nonnull Player player)
	{
		return new SmithingTableRecipeCreatorContainer(id, playerInventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(getBlockPos()));
	}
}