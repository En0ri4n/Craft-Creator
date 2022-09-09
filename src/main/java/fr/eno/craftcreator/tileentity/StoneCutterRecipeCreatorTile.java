package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.StoneCutterRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class StoneCutterRecipeCreatorTile extends InventoryContainerTileEntity
{
	public StoneCutterRecipeCreatorTile()
	{
		super(InitTileEntities.STONE_CUTTER_RECIPE_CREATOR.get(), 2);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.stone_cutter_recipe_creator.title");
	}

	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new StoneCutterRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}
}