package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.recipes.serializers.BotaniaRecipesSerializer;
import fr.eno.craftcreator.recipes.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipesSerializer;
import net.minecraft.world.item.crafting.Recipe;

public class ModRecipeCreatorDispatcher
{
    public static ModRecipesJSSerializer getSeralizer(String modId)
    {
        return switch(SupportedMods.getMod(modId))
                {
                    case BOTANIA -> BotaniaRecipesSerializer.get();
                    case THERMAL -> ThermalRecipesSerializer.get();
                    default -> MinecraftRecipeSerializer.get();
                };
    }

    public static CraftIngredients getOutput(Recipe<?> recipe)
    {
        String modId = RecipeFileUtils.getName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getOutput(recipe);
        }

        return CraftIngredients.create();
    }

    public static CraftIngredients getInputs(Recipe<?> recipe)
    {
        String modId = RecipeFileUtils.getName(recipe.getType()).getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getInput(recipe);
        }

        return CraftIngredients.create();
    }
}
