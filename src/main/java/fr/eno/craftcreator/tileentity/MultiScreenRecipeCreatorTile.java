package fr.eno.craftcreator.tileentity;

import fr.eno.craftcreator.tileentity.vanilla.TaggeableInventoryContainerTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;

    public MultiScreenRecipeCreatorTile(BlockEntityType<?> blockEntityType, BlockPos pWorldPosition, BlockState pBlockState, int slotsSize)
    {
        super(blockEntityType, pWorldPosition, pBlockState, slotsSize);
        this.screenIndex = 0;
    }

    public void setScreenIndex(int index)
    {
        this.screenIndex = index;
    }

    public int getScreenIndex()
    {
        return this.screenIndex;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("ScreenIndex", this.screenIndex);
    }

    @Override
    public void load(@NotNull CompoundTag compound)
    {
        super.load(compound);
        this.screenIndex = compound.getInt("ScreenIndex");
    }
}