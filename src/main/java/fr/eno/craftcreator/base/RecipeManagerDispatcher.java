package fr.eno.craftcreator.base;

import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.managers.BotaniaRecipesManager;
import fr.eno.craftcreator.recipes.managers.CreateRecipesManager;
import fr.eno.craftcreator.recipes.managers.MinecraftRecipeManager;
import fr.eno.craftcreator.recipes.managers.ThermalRecipesManager;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.List;

public class RecipeManagerDispatcher
{
    /**
     * @param mod               The mod to which the recipe belongs
     * @param recipe            The type of recipe to create
     * @param slots             The slots of the container
     * @param recipeInfos       The infos of the recipe (energy, experience, chance, etc...)
     * @param serializerType    The type of serializer (kubejs, etc)
     */
    public static void createRecipe(SupportedMods mod, RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        switch(mod)
        {
            case BOTANIA:
                BotaniaRecipesManager.get().createRecipe(recipe, slots, recipeInfos, serializerType);
                break;
            case THERMAL:
                ThermalRecipesManager.get().createRecipe(recipe, slots, recipeInfos, serializerType);
                break;
            case CREATE:
                CreateRecipesManager.get().createRecipe(recipe, slots, recipeInfos, serializerType);
                break;
            case MINECRAFT:
                MinecraftRecipeManager.get().createRecipe(recipe, slots, recipeInfos, serializerType);
                break;
        }
    }
}
