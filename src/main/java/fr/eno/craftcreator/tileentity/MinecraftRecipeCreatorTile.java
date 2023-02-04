package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.container.MinecraftRecipeCreatorContainer;
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

public class MinecraftRecipeCreatorTile extends MultiScreenRecipeCreatorTile
{
	private boolean shapedRecipe;

	public MinecraftRecipeCreatorTile()
	{
		super(InitTileEntities.MINECRAFT_RECIPE_CREATOR.get(), SlotHelper.MINECRAFT_SLOTS_SIZE);
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
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");
	}

	@Override
	public CompoundNBT save(CompoundNBT compoundTag)
	{
		super.save(compoundTag);
		compoundTag.putBoolean("isShapedRecipe", this.shapedRecipe);
		return compoundTag;
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName()
	{
		return new StringTextComponent("container.minecraft_recipe_creator.title");
	}

	@Nullable
	@Override
	public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player)
	{
		return new MinecraftRecipeCreatorContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()).writeBlockPos(getBlockPos()));
	}

	public boolean isShapedRecipe()
	{
		return shapedRecipe;
	}

	public void setShapedRecipe(boolean shapedRecipe)
	{
		this.shapedRecipe = shapedRecipe;
		this.setChanged();
	}
}