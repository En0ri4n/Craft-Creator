package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import io.netty.buffer.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

public class FurnaceRecipeCreatorTile extends InventoryContainerTileEntity
{
	public FurnaceRecipeCreatorTile()
	{
		super(InitTileEntities.FURNACE_RECIPE_CREATOR.get(), 3);
	}

	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new FurnaceRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(pos));
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.furnace_recipe_creator.title");
	}
}