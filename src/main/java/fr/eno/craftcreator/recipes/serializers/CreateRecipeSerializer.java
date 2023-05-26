package fr.eno.craftcreator.recipes.serializers;

import com.simibubi.create.content.contraptions.components.crusher.CrushingRecipe;
import com.simibubi.create.content.contraptions.components.saw.CuttingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
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

        if(recipe instanceof CrushingRecipe)
        {
            CrushingRecipe crushingRecipe = (CrushingRecipe) recipe;
            putIfNotEmpty(inputIngredients, crushingRecipe.getIngredients());
        }
        else if(recipe instanceof CuttingRecipe)
        {
            CuttingRecipe cuttingRecipe = (CuttingRecipe) recipe;
            putIfNotEmpty(inputIngredients, cuttingRecipe.getIngredients());
        }

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients outputIngredients = CraftIngredients.create();

        if(recipe instanceof CrushingRecipe)
        {
            CrushingRecipe crushingRecipe = (CrushingRecipe) recipe;
            for(ProcessingOutput output : crushingRecipe.getRollableResults())
                outputIngredients.addIngredient(new CraftIngredients.ItemLuckIngredient(output.getStack().getItem().getRegistryName(), output.getStack().getCount(), output.getChance()));
        }
        else if(recipe instanceof CuttingRecipe)
        {
            CuttingRecipe cuttingRecipe = (CuttingRecipe) recipe;
            for(ProcessingOutput output : cuttingRecipe.getRollableResults())
                outputIngredients.addIngredient(new CraftIngredients.ItemLuckIngredient(output.getStack().getItem().getRegistryName(), output.getStack().getCount(), output.getChance()));
        }

        return outputIngredients;
    }

    public static CreateRecipeSerializer get()
    {
        return INSTANCE;
    }
}
