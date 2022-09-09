package fr.eno.craftcreator.tileentity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

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
