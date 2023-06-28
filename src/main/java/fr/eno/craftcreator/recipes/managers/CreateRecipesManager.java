package fr.eno.craftcreator.recipes.managers;

import com.simibubi.create.content.contraptions.processing.HeatCondition;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.CreateRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.recipes.utils.SpecialRecipeEntry;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.stream.Collectors;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class CreateRecipesManager extends BaseRecipesManager
{
    private static final CreateRecipesManager INSTANCE = new CreateRecipesManager();

     //We don't use this method because Create recipes are created not with slots
    @Override
    public void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType) {}

    public void createRecipe(RecipeCreator recipe, TileEntity tileEntity, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        CreateRecipeSerializer.get().setSerializerType(serializerType);

        if(!(tileEntity instanceof CreateRecipeCreatorTile)) return;

        CreateRecipeCreatorTile createRecipeCreatorTile = (CreateRecipeCreatorTile) tileEntity;
        List<SpecialRecipeEntry> inputs = createRecipeCreatorTile.getInputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(SpecialRecipeEntry::deserialize).collect(Collectors.toList());
        List<SpecialRecipeEntry> outputs = createRecipeCreatorTile.getOutputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(SpecialRecipeEntry::deserialize).collect(Collectors.toList());

        if(areEmpty(inputs, outputs)) return;

        if(recipe.is(CRUSHING))
            serializeCrushingRecipe(inputs, outputs, recipeInfos);
        else if(recipe.is(CUTTING))
            serializeCuttingRecipe(inputs, outputs, recipeInfos);
        else if(recipe.is(MIXING))
            serializeMixingRecipe(inputs, outputs, recipeInfos);
        else if(recipe.is(MILLING))
            serializeMillingRecipe(inputs, outputs, recipeInfos);
    }

    private void serializeCrushingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        RecipeEntry.Input input = getInput(inputs.get(0));
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCrushingRecipe(input, output, processingTime);
    }

    private void serializeCuttingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        RecipeEntry.Input input = getInput(inputs.get(0));
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCuttingRecipe(input, output, processingTime);
    }

    private void serializeMixingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        RecipeEntry.MultiInput input = getValidInputs(inputs);
        RecipeEntry.Output output = getOutput(outputs.get(0));
        HeatCondition heat = HeatCondition.values()[recipeInfos.getValue("heat_requirement").intValue()];

        CreateRecipeSerializer.get().serializeMixingRecipe(input, output, heat);
    }

    private void serializeMillingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        RecipeEntry.Input input = getInput(inputs.get(0));
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeMillingRecipe(input, output, processingTime);
    }

    public static CreateRecipesManager get()
    {
        return INSTANCE;
    }
}
