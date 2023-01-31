package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.SmithingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class SmithingTableRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	public SmithingTableRecipeCreatorTile()
	{
		super(InitTileEntities.SMITHING_TABLE_RECIPE_CREATOR.get(), SlotHelper.SMITHING_TABLE_SLOTS_SIZE);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.smithing_table_recipe_creator.title");
	}

	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new SmithingTableRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getPos()));
	}
}