package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.components.crusher.CrushingRecipe;
import com.simibubi.create.content.contraptions.components.mixer.MixingRecipe;
import com.simibubi.create.content.contraptions.components.saw.CuttingRecipe;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
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

        obj.add("ingredients", getInputArray(input)); // Only one ingredient is allowed
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeCuttingRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput output, int processingTime)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.CUTTING.getType());

        obj.add("ingredients", getInputArray(input)); // Only one ingredient is allowed
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

        obj.add("ingredients", getInputArray(input)); // Only one ingredient is allowed
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    public void serializeCompactingRecipe(RecipeEntry.MultiInput input, RecipeEntry.MultiOutput output, int processingTime)
    {
        JsonObject obj = createBaseJson(AllRecipeTypes.COMPACTING.getType());

        obj.add("ingredients", getInputArray(input.get(0))); // Only one ingredient is allowed
        obj.add("results", getResultArray(output));
        obj.addProperty("processingTime", processingTime);

        addRecipeTo(obj, output.getOneOutput().getRegistryName());
    }

    @Override
    public CraftIngredients getInput(IRecipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        ProcessingRecipe<RecipeWrapper> processRecipe = (ProcessingRecipe<RecipeWrapper>) recipe;
        putIfNotEmpty(inputIngredients, processRecipe.getIngredients());

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
