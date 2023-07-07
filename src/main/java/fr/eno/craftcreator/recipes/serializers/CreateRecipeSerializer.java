package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CreateRecipeSerializer extends ModRecipeSerializer
{
    private static final CreateRecipeSerializer INSTANCE = new CreateRecipeSerializer();

    private CreateRecipeSerializer()
    {
        super(SupportedMods.CREATE);
    }

    public void serializeCrushingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output, int processingTime)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.CRUSHING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeCuttingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output, int processingTime)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.CUTTING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeMixingRecipe(RecipeEntry.MultiInput input, RecipeEntry.Output output, HeatCondition heatRequirement)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.MIXING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));
        if(heatRequirement != HeatCondition.NONE)
            obj.addProperty("heatRequirement", heatRequirement.serialize());

        addRecipeTo(obj, output.getRegistryName());
    }

    public void serializeMillingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output, int processingTime)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.MILLING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeCompactingRecipe(RecipeEntry.MultiInput input, RecipeEntry.Output output)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.COMPACTING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));

        addRecipeTo(obj, output.getRegistryName());
    }

    public void serializePressingRecipe(RecipeEntry.MultiInput input, RecipeEntry.Output output)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.PRESSING.getType());

        JsonArray ingredients = new JsonArray();
        if(input.size() <= 1)
            ingredients = getInputArray(input); // An array of one ingredient
        else
            ingredients.add(getInputArray(input)); // An array of an array of ingredient for multiple ingredients

        obj.add("ingredients", ingredients);        // The press recipe allow an array of an array of ingredient, because it's only one input but multiple ingredients
        obj.add("results", getResultArray(output)); // allowed, like a tag but with different items

        addRecipeTo(obj, output.getRegistryName());
    }

    public void serializeFillingRecipe(RecipeEntry.MultiInput input, RecipeEntry.Output output)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.FILLING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));

        addRecipeTo(obj, output.getRegistryName());
    }

    public void serializeSplashingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.SPLASHING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeEmptyingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.EMPTYING.getType());

        obj.add("ingredients", getInputArray(input));
        obj.add("results", getResultArray(output));

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    @Override
    public CraftIngredients getInput(IRecipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        ProcessingRecipe<RecipeWrapper> processRecipe = (ProcessingRecipe<RecipeWrapper>) recipe;
        putIfNotEmpty(inputIngredients, processRecipe.getIngredients());

        if(!processRecipe.getFluidIngredients().isEmpty())
            for(FluidIngredient fluidIngredient : processRecipe.getFluidIngredients())
                inputIngredients.addIngredient(new CraftIngredients.FluidIngredient(fluidIngredient.getMatchingFluidStacks().get(0).getFluid().getRegistryName(), fluidIngredient.getRequiredAmount()));

        if(processRecipe.getProcessingDuration() != 0)
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Processing Time", CraftIngredients.DataIngredient.DataUnit.TICK, processRecipe.getProcessingDuration(), false));

        if(processRecipe.getRequiredHeat() != HeatCondition.NONE)
            inputIngredients.addIngredient(new CraftIngredients.StringDataIngredient("Heat", processRecipe.getRequiredHeat().serialize()));

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(IRecipe<?> recipe)
    {
        CraftIngredients outputIngredients = CraftIngredients.create();

        ProcessingRecipe<RecipeWrapper> processRecipe = (ProcessingRecipe<RecipeWrapper>) recipe;

        for(ProcessingOutput output : processRecipe.getRollableResults())
            outputIngredients.addIngredient(new CraftIngredients.ItemLuckIngredient(output.getStack().getItem().getRegistryName(), output.getStack().getCount(), output.getChance()));

        for(FluidStack fluidOutput : processRecipe.getFluidResults())
            outputIngredients.addIngredient(new CraftIngredients.FluidIngredient(fluidOutput.getFluid().getRegistryName(), fluidOutput.getAmount()));

        return outputIngredients;
    }

    public static CreateRecipeSerializer get()
    {
        return INSTANCE;
    }
}
