package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class CreateRecipesManager extends BaseRecipesManager
{
    private static final CreateRecipesManager INSTANCE = new CreateRecipesManager();

    @Override
    public void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        // recipe.doIfIs(CRUSHING, v -> serializeCrushingRecipe(slots, recipeInfos, serializerType));
    }

    public static CreateRecipesManager get()
    {
        return INSTANCE;
    }
}
