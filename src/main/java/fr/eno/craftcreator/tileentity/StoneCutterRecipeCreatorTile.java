package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import io.netty.buffer.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

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