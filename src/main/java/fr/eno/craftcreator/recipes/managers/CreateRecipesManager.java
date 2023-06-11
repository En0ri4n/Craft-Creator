package fr.eno.craftcreator.recipes.managers;

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
        List<SpecialRecipeEntry> inputs = createRecipeCreatorTile.getInputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(SpecialRecipeEntry::deserialize).collect(Collectors.toList());
        List<SpecialRecipeEntry> outputs = createRecipeCreatorTile.getOutputs().get(recipe.getRecipeTypeLocation().getPath()).stream().map(SpecialRecipeEntry::deserialize).collect(Collectors.toList());

        if(recipe.is(CRUSHING))
            serializeCrushingRecipe(inputs, outputs, recipeInfos);
        else if(recipe.is(CUTTING))
            serializeCuttingRecipe(inputs, outputs, recipeInfos);
    }

    public RecipeEntry.MultiInput getValidInputs(List<SpecialRecipeEntry> inputs)
    {
        RecipeEntry.MultiInput multiInput = new RecipeEntry.MultiInput();
        inputs.forEach(sre -> multiInput.add(new RecipeEntry.Input(sre.isTag(), sre.getRegistryName(), sre.getCount())));
        return multiInput;
    }

    public RecipeEntry.MultiOutput getValidOutputs(List<SpecialRecipeEntry> outputs)
    {
        RecipeEntry.MultiOutput multiOutput = new RecipeEntry.MultiOutput();
        outputs.forEach(sre -> multiOutput.add(new RecipeEntry.LuckedOutput(sre.getRegistryName(), sre.getCount(), sre.getChance())));
        return multiOutput;
    }

    protected boolean areEmpty(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs)
    {
        boolean hasNoInput = true;
        boolean hasNoOutput = true;

        for(SpecialRecipeEntry input : inputs)
            if(!input.isEmpty())
            {
                hasNoInput = false;
                break;
            }

        for(SpecialRecipeEntry output : outputs)
            if(!output.isEmpty())
            {
                hasNoOutput = false;
                break;
            }

        return hasNoInput || hasNoOutput;
    }

    private void serializeCrushingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        if(areEmpty(inputs, outputs)) return;

        RecipeEntry.MultiInput input = getValidInputs(inputs);
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCrushingRecipe(input, output, processingTime);
    }

    private void serializeCuttingRecipe(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs, RecipeInfos recipeInfos)
    {
        if(areEmpty(inputs, outputs)) return;

        RecipeEntry.MultiInput input = getValidInputs(inputs);
        RecipeEntry.MultiOutput output = getValidOutputs(outputs);
        int processingTime = recipeInfos.getValue("processing_time").intValue();

        CreateRecipeSerializer.get().serializeCuttingRecipe(input, output, processingTime);
    }

    public static CreateRecipesManager get()
    {
        return INSTANCE;
    }
}
