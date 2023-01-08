package fr.eno.craftcreator.tileentity.vanilla;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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

    public TaggeableInventoryContainerTileEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int inventorySize)
    {
        super(pType, pWorldPosition, pBlockState, inventorySize);
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
    public void load(@NotNull CompoundTag compound)
    {
        super.load(compound);

        if(compound.contains(TAGGED_SLOTS_TAG))
        {
            ListTag list = (ListTag) compound.get(TAGGED_SLOTS_TAG);

            if(list != null)
            {
                for(Tag nbt : list)
                {
                    CompoundTag compoundNBT = (CompoundTag) nbt;

                    this.taggedSlots.put(compoundNBT.getInt("Slot"), ResourceLocation.tryParse(compoundNBT.getString("Tag")));
                }
            }
        }
        if(compound.contains(NBT_SLOTS_TAG))
        {
            IntArrayTag list = (IntArrayTag) compound.get(NBT_SLOTS_TAG);

            for(int i : list.getAsIntArray())
            {
                this.nbtSlots.add(i);
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);

        ListTag taggedSlotsListTag = new ListTag();
        for(Integer integer : this.taggedSlots.keySet())
        {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putInt("Slot", integer);
            compoundNBT.putString("Tag", this.taggedSlots.get(integer).toString());
            taggedSlotsListTag.add(compoundNBT);
        }
        compoundTag.put(TAGGED_SLOTS_TAG, taggedSlotsListTag);

        IntArrayTag nbtSlotsListTag = new IntArrayTag(this.nbtSlots.stream().mapToInt(i -> i).toArray());
        compoundTag.put(NBT_SLOTS_TAG, nbtSlotsListTag);
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
