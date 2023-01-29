package fr.eno.craftcreator.tileentity.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;
    private boolean isKubeJSRecipe;

    public MultiScreenRecipeCreatorTile(BlockEntityType<?> blockEntityType, BlockPos pWorldPosition, BlockState pBlockState, int slotsSize)
    {
        super(blockEntityType, pWorldPosition, pBlockState, slotsSize);
        this.screenIndex = 0;
        this.isKubeJSRecipe = true;
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        if(dataName.equals("screen_index"))
        {
            this.setScreenIndex((Integer) data);
        }
        else if(dataName.equals("kubejs_recipe"))
            this.setKubeJSRecipe((Boolean) data);
    }

    public void setKubeJSRecipe(boolean isKubeJSRecipe)
    {
        this.isKubeJSRecipe = isKubeJSRecipe;
    }

    @Override
    public Object getData(String dataName)
    {
        if(dataName.equals("screen_index"))
            return this.getScreenIndex();
        else if(dataName.equals("kubejs_recipe"))
            return this.isKubeJSRecipe();

        return super.getData(dataName);
    }

    private boolean isKubeJSRecipe()
    {
        return isKubeJSRecipe;
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
        compoundTag.putBoolean("KubeJSRecipe", this.isKubeJSRecipe);
    }

    @Override
    public void load(@NotNull CompoundTag compound)
    {
        super.load(compound);
        this.screenIndex = compound.getInt("ScreenIndex");
        this.isKubeJSRecipe = compound.getBoolean("KubeJSRecipe");
    }
}
