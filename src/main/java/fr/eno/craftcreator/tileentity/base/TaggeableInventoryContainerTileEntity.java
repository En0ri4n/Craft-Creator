package fr.eno.craftcreator.tileentity.base;

import fr.eno.craftcreator.api.ClientUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TaggeableInventoryContainerTileEntity extends InventoryDataContainerTileEntity
{
    private static final String TAGGED_SLOTS_TAG = "TaggedSlots";
    private static final String NBT_SLOTS_TAG = "NbtSlots";
    private Map<Integer, ResourceLocation> taggedSlots;
    private List<Integer> nbtSlots;

    public TaggeableInventoryContainerTileEntity(TileEntityType<?> pType, int inventorySize)
    {
        super(pType, inventorySize);
        this.taggedSlots = new HashMap<>();
        this.nbtSlots = new ArrayList<>();
    }

    @Override
    public Object getData(String dataName)
    {
        if(dataName.equals("tagged_slots"))
            return this.getTaggedSlots();
        else if(dataName.equals("nbt_slots"))
            return this.getNbtSlots();

        return null;
    }

    private int[] getNbtSlots()
    {
        int[] slots = new int[this.nbtSlots.size()];
        for(int i = 0; i < this.nbtSlots.size(); i++)
            slots[i] = this.nbtSlots.get(i);
        return slots;
    }

    @Override
    public void setData(String dataName, Object data)
    {
        if(dataName.equals("tagged_slots"))
        {
            this.setTaggedSlots((Map<Integer, ResourceLocation>) data);
        }
        else if(dataName.equals("nbt_slots"))
        {
            this.setNbtSlots((int[]) data);
        }
    }

    private void setNbtSlots(int[] nbtSlots)
    {
        this.nbtSlots.clear();

        for(int i : nbtSlots)
        {
            this.nbtSlots.add(i);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT compound)
    {
        super.load(state, compound);

        if(compound.contains(TAGGED_SLOTS_TAG))
        {
            ListNBT list = (ListNBT) compound.get(TAGGED_SLOTS_TAG);

            if(list != null)
            {
                for(INBT nbt : list)
                {
                    CompoundNBT compoundNBT = (CompoundNBT) nbt;

                    this.taggedSlots.put(compoundNBT.getInt("Slot"), ClientUtils.parse(compoundNBT.getString("Tag")));
                }
            }
        }
        if(compound.contains(NBT_SLOTS_TAG))
        {
            IntArrayNBT list = (IntArrayNBT) compound.get(NBT_SLOTS_TAG);

            for(int i : list.getAsIntArray())
            {
                this.nbtSlots.add(i);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundTag)
    {
        super.save(compoundTag);

        ListNBT taggedSlotsListNBT = new ListNBT();
        for(Integer integer : this.taggedSlots.keySet())
        {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("Slot", integer);
            compoundNBT.putString("Tag", this.taggedSlots.get(integer).toString());
            taggedSlotsListNBT.add(compoundNBT);
        }
        compoundTag.put(TAGGED_SLOTS_TAG, taggedSlotsListNBT);

        IntArrayNBT nbtSlotsListNBT = new IntArrayNBT(this.nbtSlots.stream().mapToInt(i -> i).toArray());
        compoundTag.put(NBT_SLOTS_TAG, nbtSlotsListNBT);

        return compoundTag;
    }

    public Map<Integer, ResourceLocation> getTaggedSlots()
    {
        return taggedSlots;
    }

    public void setTaggedSlots(Map<Integer, ResourceLocation> taggedSlots)
    {
        this.taggedSlots = taggedSlots;
        this.setChanged();
    }
}
