package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.recipes.serializers.BotaniaRecipesSerializer;
import fr.eno.craftcreator.recipes.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipesSerializer;
import net.minecraft.item.crafting.IRecipe;

public class ModRecipeCreatorDispatcher
{
    public static ModRecipesJSSerializer getSeralizer(String modId)
    {
        switch(SupportedMods.getMod(modId))
        {
            case BOTANIA:
                return BotaniaRecipesSerializer.get();
            case THERMAL:
                return ThermalRecipesSerializer.get();
            default:
                return MinecraftRecipeSerializer.get();
        }
    }

    public static CraftIngredients getOutput(IRecipe<?> recipe)
    {
        String modId = RecipeFileUtils.getName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getOutput(recipe);
        }

        return CraftIngredients.create();
    }

    public static CraftIngredients getInputs(IRecipe<?> recipe)
    {
        String modId = RecipeFileUtils.getName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getInput(recipe);
        }

        return CraftIngredients.create();
    }
}
