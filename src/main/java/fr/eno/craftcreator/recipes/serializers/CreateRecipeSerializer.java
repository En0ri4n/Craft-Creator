package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import net.minecraft.world.item.crafting.Recipe;

// TODO: add support for fixed size recipes in RecipeEntryWidget to prevent the user from adding more than one ingredient
public class CreateRecipeSerializer extends ModRecipeSerializer
{
    private static final CreateRecipeSerializer INSTANCE = new CreateRecipeSerializer();

    private CreateRecipeSerializer()
    {
        super(SupportedMods.CREATE);
    }

    public void serializeCrushingRecipe(RecipeEntry.MultiInput input, RecipeEntry.MultiOutput output, int processingTime, SerializerType serializerType)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.CRUSHING.getType());

        obj.add("ingredients", getInputArray(input.get(0))); // Only one ingredient is allowed
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, AllRecipeTypes.CRUSHING.getType(), output.getOneOutput().getRegistryName());
    }

    public void serializeCuttingRecipe(RecipeEntry.MultiInput input, RecipeEntry.MultiOutput output, int processingTime, SerializerType serializerType)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.CUTTING.getType());

        obj.add("ingredients", getInputArray(input.get(0))); // Only one ingredient is allowed
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, AllRecipeTypes.CUTTING.getType(), output.getOneOutput().getRegistryName());
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof CrushingRecipe)
        {
            CrushingRecipe crushingRecipe = (CrushingRecipe) recipe;
            putIfNotEmpty(inputIngredients, crushingRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Processing Time", CraftIngredients.DataIngredient.DataUnit.TICK, crushingRecipe.getProcessingDuration(), false));
        }
        else if(recipe instanceof CuttingRecipe)
        {
            CuttingRecipe cuttingRecipe = (CuttingRecipe) recipe;
            putIfNotEmpty(inputIngredients, cuttingRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Processing Time", CraftIngredients.DataIngredient.DataUnit.TICK, cuttingRecipe.getProcessingDuration(), false));
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
