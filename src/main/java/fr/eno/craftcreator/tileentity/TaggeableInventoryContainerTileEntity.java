package fr.eno.craftcreator.tileentity;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class TaggeableInventoryContainerTileEntity extends InventoryContainerTileEntity
{
    private Map<Integer, ResourceLocation> taggedSlots;

    public TaggeableInventoryContainerTileEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int inventorySize)
    {
        super(pType, pWorldPosition, pBlockState, inventorySize);
        this.taggedSlots = new HashMap<>();
    }

    @Override
    public void load(@NotNull CompoundTag compound)
    {
        super.load(compound);

        this.taggedSlots.clear();

        if(compound.contains("TaggedSlots"))
        {
            ListTag list = (ListTag) compound.get("TaggedSlots");

            if(list != null)
            {
                for(Tag nbt : list)
                {
                    CompoundTag compoundNBT = (CompoundTag) nbt;

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
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);

        ListTag list = new ListTag();

        for(Integer integer : this.taggedSlots.keySet())
        {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putInt("Slot", integer);
            compoundNBT.putString("Tag", this.taggedSlots.get(integer).toString());
            list.add(compoundNBT);
        }

        compoundTag.put("TaggedSlots", list);
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
