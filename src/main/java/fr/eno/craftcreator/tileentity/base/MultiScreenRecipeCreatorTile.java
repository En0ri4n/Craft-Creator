package fr.eno.craftcreator.tileentity.base;

import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreators;
import fr.eno.craftcreator.base.SupportedMods;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;
    private ResourceLocation currentRecipeType;
    private boolean isKubeJSRecipe;
    private double[] fields;

    public MultiScreenRecipeCreatorTile(SupportedMods mod, TileEntityType<?> blockEntityType, int slotsSize)
    {
        super(blockEntityType, slotsSize);
        this.currentRecipeType = ModRecipeCreators.getRecipeCreatorScreens(mod).get(0).getRecipeTypeLocation();
        this.screenIndex = 0;
        this.isKubeJSRecipe = true;
        this.fields = new double[1];
    }

    @Override
    public void setData(String dataName, Object data)
    {
        super.setData(dataName, data);

        switch(dataName)
        {
            case "screen_index":
                this.setScreenIndex((Integer) data);
                break;
            case "kubejs_recipe":
                this.setKubeJSRecipe((Boolean) data);
                break;
            case "recipe_type":
                this.setCurrentRecipeType(CommonUtils.parse((String) data));
                break;
            case "fields":
                this.setFields((double[]) data);
                break;
        }
    }

    private void setFields(double[] fields)
    {
        this.fields = fields;
    }

    public void setCurrentRecipeType(ResourceLocation recipeType)
    {
        this.currentRecipeType = recipeType;
    }

    public void setKubeJSRecipe(boolean isKubeJSRecipe)
    {
        this.isKubeJSRecipe = isKubeJSRecipe;
    }

    @Override
    public Object getData(String dataName)
    {
        switch(dataName)
        {
            case "screen_index":
                return this.getScreenIndex();
            case "kubejs_recipe":
                return this.isKubeJSRecipe();
            case "recipe_type":
                return this.getCurrentRecipeType().toString();
            case "fields":
                return this.getFields();
        }

        return super.getData(dataName);
    }

    private double[] getFields()
    {
        return fields;
    }

    public ResourceLocation getCurrentRecipeType()
    {
        return this.currentRecipeType;
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
    public CompoundNBT save(CompoundNBT compoundTag)
    {
        super.save(compoundTag);
        compoundTag.putInt("ScreenIndex", this.screenIndex);
        compoundTag.putBoolean("KubeJSRecipe", this.isKubeJSRecipe);
        compoundTag.putString("RecipeType", this.currentRecipeType.toString());
        compoundTag.putLongArray("Fields", Arrays.stream(this.fields).mapToLong(Double::doubleToLongBits).toArray());
        return compoundTag;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound)
    {
        super.load(state, compound);
        this.screenIndex = compound.getInt("ScreenIndex");
        this.isKubeJSRecipe = compound.getBoolean("KubeJSRecipe");
        this.currentRecipeType = CommonUtils.parse(compound.getString("RecipeType"));
        this.fields = Arrays.stream(compound.getLongArray("Fields")).mapToDouble(Double::longBitsToDouble).toArray();
    }
}
