package fr.eno.craftcreator.tileentity;

import com.mojang.brigadier.*;
import com.mojang.brigadier.exceptions.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

import javax.annotation.*;
import java.util.*;

public abstract class TaggeableInventoryContainerTileEntity extends InventoryContainerTileEntity
{
    private Map<Integer, ResourceLocation> taggedSlots;

    public TaggeableInventoryContainerTileEntity(TileEntityType<?> type, int inventorySize)
    {
        super(type, inventorySize);
        this.taggedSlots = new HashMap<>();
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound)
    {
        super.read(state, compound);

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

    public Map<Integer, ResourceLocation> getTaggedSlots()
    {
        return taggedSlots;
    }

    public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
    {
        this.taggedSlots = taggedSlots;
        this.markDirty();
    }
}
