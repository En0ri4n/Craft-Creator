package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import io.netty.buffer.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.text.*;

import javax.annotation.*;

public class CraftingTableRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
	private boolean shapedRecipe;

	public CraftingTableRecipeCreatorTile()
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), 10);
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound)
	{
		super.read(state, compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound)
	{
		compound.putBoolean("isShapedRecipe", this.shapedRecipe);

		return super.write(compound);
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

	public boolean isShapedRecipe()
	{
		return shapedRecipe;
	}

	public void setShapedRecipe(boolean shapedRecipe)
	{
		this.shapedRecipe = shapedRecipe;
		this.markDirty();
	}
}