package fr.eno.craftcreator.tileentity.vanilla;

import fr.eno.craftcreator.container.CraftingTableRecipeCreatorContainer;
import fr.eno.craftcreator.init.InitTileEntities;
import fr.eno.craftcreator.tileentity.utils.MultiScreenRecipeCreatorTile;
import fr.eno.craftcreator.utils.SlotHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingTableRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	private boolean shapedRecipe;

	public CraftingTableRecipeCreatorTile()
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), SlotHelper.CRAFTING_TABLE_SLOTS_SIZE);
	}

	@Override
	public void setData(String dataName, Object data)
	{
		super.setData(dataName, data);
		if(dataName.equals("shaped"))
			setShapedRecipe((boolean) data);
	}

	@Override
	public Object getData(String dataName)
	{
		if(dataName.equals("shaped"))
			return isShapedRecipe();

		return super.getData(dataName);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound)
	{
		super.read(state, compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");
	}

	@Override
	public CompoundNBT write(CompoundNBT compoundTag)
	{
		super.write(compoundTag);
		compoundTag.putBoolean("isShapedRecipe", this.shapedRecipe);
		return compoundTag;
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