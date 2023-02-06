package fr.eno.craftcreator.tileentity.base;

import fr.eno.craftcreator.recipes.utils.RecipeFileUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.base.ModRecipeCreator;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public abstract class MultiScreenRecipeCreatorTile extends TaggeableInventoryContainerTileEntity
{
    private int screenIndex;
    private ResourceLocation currentRecipeType;
    private boolean isKubeJSRecipe;

    public MultiScreenRecipeCreatorTile(SupportedMods mod, TileEntityType<?> blockEntityType, int slotsSize)
    {
        super(blockEntityType, slotsSize);
        this.currentRecipeType = RecipeFileUtils.getRecipeTypeName(ModRecipeCreator.getRecipeCreatorScreens(mod).get(0).getRecipeType());
        this.screenIndex = 0;
        this.isKubeJSRecipe = true;
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
                this.setCurrentRecipeType(ResourceLocation.tryParse((String) data));
                break;
        }
    }

    private void setCurrentRecipeType(ResourceLocation recipeType)
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
        }

        return super.getData(dataName);
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
        return compoundTag;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound)
    {
        super.load(state, compound);
        this.screenIndex = compound.getInt("ScreenIndex");
        this.isKubeJSRecipe = compound.getBoolean("KubeJSRecipe");
        this.currentRecipeType = ResourceLocation.tryParse(compound.getString("RecipeType"));
    }
}
