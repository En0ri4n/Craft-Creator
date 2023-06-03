package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.CreateRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.widgets.RecipeEntryWidget;
import fr.eno.craftcreator.tileentity.CreateRecipeCreatorTile;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
import java.util.stream.Collectors;

import static fr.eno.craftcreator.base.ModRecipeCreators.CRUSHING;
import static fr.eno.craftcreator.base.ModRecipeCreators.CUTTING;

public class CreateRecipesManager extends BaseRecipesManager
{
    private static final CreateRecipesManager INSTANCE = new CreateRecipesManager();

     //We don't use this method because Create recipes are created not with slots but with another way
    @Override
    public void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType) {}

    public void createRecipe(RecipeCreator recipe, TileEntity tileEntity, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        CreateRecipeSerializer.get().setSerializerType(serializerType);

        if(!(tileEntity instanceof CreateRecipeCreatorTile)) return;

        CreateRecipeCreatorTile createRecipeCreatorTile = (CreateRecipeCreatorTile) tileEntity;
        List<RecipeEntryWidget.RecipeEntryEntry> inputs = createRecipeCreatorTile.getInputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(RecipeEntryWidget.RecipeEntryEntry::deserialize).collect(Collectors.toList());
        List<RecipeEntryWidget.RecipeEntryEntry> outputs = createRecipeCreatorTile.getOutputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(RecipeEntryWidget.RecipeEntryEntry::deserialize).collect(Collectors.toList());

        if(recipe.is(CRUSHING))
            serializeCrushingRecipe(inputs, outputs, recipeInfos, serializerType);
        else if(recipe.is(CUTTING))
            serializeCuttingRecipe(inputs, outputs, recipeInfos, serializerType);
    }

    public RecipeEntry.MultiInput getValidInputs(List<RecipeEntryWidget.RecipeEntryEntry> inputs)
    {
        RecipeEntry.MultiInput multiInput = new RecipeEntry.MultiInput();
        inputs.forEach(ree -> multiInput.add(new RecipeEntry.Input(ree.isTag(), ree.getRegistryName(), ree.getCount())));
        return multiInput;
    }

    public RecipeEntry.MultiOutput getValidOutputs(List<RecipeEntryWidget.RecipeEntryEntry> outputs)
    {
        RecipeEntry.MultiOutput multiOutput = new RecipeEntry.MultiOutput();
        outputs.forEach(ree -> multiOutput.add(new RecipeEntry.LuckedOutput(ree.getRegistryName(), ree.getCount(), ree.getChance())));
        return multiOutput;
    }

    protected boolean areEmpty(List<RecipeEntryWidget.RecipeEntryEntry> inputs, List<RecipeEntryWidget.RecipeEntryEntry> outputs)
    {
        boolean hasNoInput = true;
        boolean hasNoOutput = true;

        for(RecipeEntryWidget.RecipeEntryEntry input : inputs)
            if(!input.isEmpty())
            {
                hasNoInput = false;
                break;
            }

        for(RecipeEntryWidget.RecipeEntryEntry output : outputs)
            if(!output.isEmpty())
            {
                hasNoOutput = false;
                break;
            }

        return hasNoInput || hasNoOutput;
    }

    private void serializeCrushingRecipe(List<RecipeEntryWidget.RecipeEntryEntry> inputs, List<RecipeEntryWidget.RecipeEntryEntry> outputs, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        if(areEmpty(inputs, outputs)) return;

        RecipeEntry.MultiInput input = getValidInputs(inputs);
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCrushingRecipe(input, output, processingTime, serializerType);
    }

    private void serializeCuttingRecipe(List<RecipeEntryWidget.RecipeEntryEntry> inputs, List<RecipeEntryWidget.RecipeEntryEntry> outputs, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        if(areEmpty(inputs, outputs)) return;

        RecipeEntry.MultiInput input = getValidInputs(inputs);
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCuttingRecipe(input, output, processingTime, serializerType);
    }

    public static CreateRecipesManager get()
    {
        return INSTANCE;
    }
}
