package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import io.netty.buffer.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

public class CraftingTableRecipeCreatorTile extends InventoryContainerTileEntity
{
	public CraftingTableRecipeCreatorTile()
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), 10);
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.crafting_table_recipe_creator.title");
	}

	@Nullable
	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new CraftingTableRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getPos()));
	}
}