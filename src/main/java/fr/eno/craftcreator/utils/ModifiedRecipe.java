package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.kubejs.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

import java.util.HashMap;
import java.util.Map;

public class ModifiedRecipe
{
    private final RecipeFileUtils.ModifiedRecipeType type;
    private final Map<ModRecipesJSSerializer.RecipeDescriptors, String> recipeDescriptors;

    public ModifiedRecipe(RecipeFileUtils.ModifiedRecipeType type)
    {
        this.type = type;
        this.recipeDescriptors = new HashMap<>();
    }

    public ModifiedRecipe(RecipeFileUtils.ModifiedRecipeType type, Map<ModRecipesJSSerializer.RecipeDescriptors, String> recipeDescriptors)
    {
        this.type = type;
        this.recipeDescriptors = recipeDescriptors;
    }

    public String getInputItem()
    {
        return recipeDescriptors.get(ModRecipesJSSerializer.RecipeDescriptors.INPUT_ITEM);
    }

    public String getOutputItem()
    {
        return recipeDescriptors.get(ModRecipesJSSerializer.RecipeDescriptors.OUTPUT_ITEM);
    }

    public String getModId()
    {
        return recipeDescriptors.get(ModRecipesJSSerializer.RecipeDescriptors.MOD_ID);
    }

    public String getRecipeType()
    {
        return recipeDescriptors.get(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_TYPE);
    }

    public String getRecipeId()
    {
        return recipeDescriptors.get(ModRecipesJSSerializer.RecipeDescriptors.RECIPE_ID);
    }

    public <C extends Container> Recipe<C> getRecipeIfExists()
    {
        return null;
    }

    public void setDescriptor(ModRecipesJSSerializer.RecipeDescriptors descriptor, String value)
    {
        recipeDescriptors.put(descriptor, value);
    }

    public Map<ModRecipesJSSerializer.RecipeDescriptors, String> getRecipeMap()
    {
        return this.recipeDescriptors;
    }

    public RecipeFileUtils.ModifiedRecipeType getType()
    {
        return type;
    }
}
