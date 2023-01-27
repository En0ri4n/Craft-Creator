package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Objects;

public class MinecraftRecipeManager extends BaseRecipesManager
{
    private static final MinecraftRecipeManager INSTANCE = new MinecraftRecipeManager();
    @Override
    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        if(Objects.requireNonNull(recipe) == ModRecipeCreator.CRAFTING_TABLE)
        {
            MinecraftRecipeSerializer.createCraftingTableRecipe(slots, recipeInfos);
        }
    }

    public static MinecraftRecipeManager get()
    {
        return INSTANCE;
    }
}
