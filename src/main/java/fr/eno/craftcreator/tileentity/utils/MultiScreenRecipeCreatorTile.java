package fr.eno.craftcreator.tileentity.utils;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;
    private boolean isKubeJSRecipe;

    public MultiScreenRecipeCreatorTile(TileEntityType<?> blockEntityType, int slotsSize)
    {
        super(blockEntityType, slotsSize);
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
    public CompoundNBT write(CompoundNBT compoundTag)
    {
        super.write(compoundTag);
        compoundTag.putInt("ScreenIndex", this.screenIndex);
        compoundTag.putBoolean("KubeJSRecipe", this.isKubeJSRecipe);
        return compoundTag;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound)
    {
        super.read(state, compound);
        this.screenIndex = compound.getInt("ScreenIndex");
        this.isKubeJSRecipe = compound.getBoolean("KubeJSRecipe");
    }
}
