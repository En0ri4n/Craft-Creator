package fr.eno.craftcreator.tileentity.base;


import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;
    private ResourceLocation currentRecipeType;
    private boolean isKubeJSRecipe;
    private double[] fields;

    public MultiScreenRecipeCreatorTile(SupportedMods mod, BlockEntityType<?> blockEntityType, int slotsSize, BlockPos pos, BlockState state)
    {
        super(blockEntityType, slotsSize, pos, state);
        this.currentRecipeType = CommonUtils.getRecipeTypeName(ModRecipeCreator.getRecipeCreatorScreens(mod).get(0).getRecipeType());
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
                this.setCurrentRecipeType(ClientUtils.parse((String) data));
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
    public void saveAdditional(CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("ScreenIndex", this.screenIndex);
        compoundTag.putBoolean("KubeJSRecipe", this.isKubeJSRecipe);
        compoundTag.putString("RecipeType", this.currentRecipeType.toString());
        compoundTag.putLongArray("Fields", Arrays.stream(this.fields).mapToLong(Double::doubleToLongBits).toArray());
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        this.screenIndex = compound.getInt("ScreenIndex");
        this.isKubeJSRecipe = compound.getBoolean("KubeJSRecipe");
        this.currentRecipeType = ClientUtils.parse(compound.getString("RecipeType"));
        this.fields = Arrays.stream(compound.getLongArray("Fields")).mapToDouble(Double::longBitsToDouble).toArray();
    }
}
