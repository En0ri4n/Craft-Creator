package fr.eno.craftcreator.tileentity;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import fr.eno.craftcreator.container.*;
import fr.eno.craftcreator.init.*;
import io.netty.buffer.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;

import javax.annotation.*;
import java.util.*;

public class CraftingTableRecipeCreatorTile extends InventoryContainerTileEntity
{
	private boolean shapedRecipe;
	private Map<Integer, ResourceLocation> taggedSlots;

	public CraftingTableRecipeCreatorTile()
	{
		super(InitTileEntities.CRAFTING_TABLE_RECIPE_CREATOR.get(), 10);
		this.taggedSlots = new HashMap<>();
	}

	@Override
	public void read(@Nonnull CompoundNBT compound)
	{
		super.read(compound);

		if(compound.contains("isShapedRecipe"))
			this.shapedRecipe = compound.getBoolean("isShapedRecipe");

		this.taggedSlots.clear();

		if(compound.contains("TaggedSlots"))
		{
			ListNBT list = (ListNBT) compound.get("TaggedSlots");

			for(INBT nbt : list)
			{
				CompoundNBT compoundNBT = (CompoundNBT) nbt;

				try
				{
					this.taggedSlots.put(compoundNBT.getInt("Slot"), ResourceLocation.read(new StringReader(compoundNBT.getString("Tag"))));
				}
				catch(CommandSyntaxException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound)
	{
		compound.putBoolean("isShapedRecipe", this.shapedRecipe);

		ListNBT list = new ListNBT();

		for(Integer integer : this.taggedSlots.keySet())
		{
			CompoundNBT compoundNBT = new CompoundNBT();
			compoundNBT.putInt("Slot", integer);
			compoundNBT.putString("Tag", this.taggedSlots.get(integer).toString());
			list.add(compoundNBT);
		}

		compound.put("TaggedSlots", list);

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

	public Map<Integer, ResourceLocation> getTaggedSlots()
	{
		return taggedSlots;
	}

	public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
	{
		this.taggedSlots = taggedSlots;
		this.markDirty();
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