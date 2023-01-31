package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import net.minecraft.inventory.container.Slot;

import java.util.List;

public class RecipeManagerDispatcher
{
    /**
     * @param mod         The mod to which the recipe belongs
     * @param recipe      The type of recipe to create
     * @param slots       The slots of the container
     * @param recipeInfos The infos of the recipe (energy, experience, chance, etc...)
     */
    public static void createRecipe(SupportedMods mod, ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        switch(mod)
        {
            case BOTANIA:
                BotaniaRecipesManager.get().createRecipe(recipe, slots, recipeInfos);
                break;
            case THERMAL:
                ThermalRecipesManager.get().createRecipe(recipe, slots, recipeInfos);
                break;
            case MINECRAFT:
                MinecraftRecipeManager.get().createRecipe(recipe, slots, recipeInfos);
                break;
        }
    }
}
