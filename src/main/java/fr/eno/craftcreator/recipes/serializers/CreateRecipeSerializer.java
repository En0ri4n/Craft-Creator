package fr.eno.craftcreator.recipes.serializers;


import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import net.minecraft.world.item.crafting.Recipe;

public class CreateRecipeSerializer extends ModRecipeSerializer
{
    private static final CreateRecipeSerializer INSTANCE = new CreateRecipeSerializer();

    private CreateRecipeSerializer()
    {
        super(SupportedMods.CREATE);
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients outputIngredients = CraftIngredients.create();

        return outputIngredients;
    }

    public static CreateRecipeSerializer get()
    {
        return INSTANCE;
    }
}
