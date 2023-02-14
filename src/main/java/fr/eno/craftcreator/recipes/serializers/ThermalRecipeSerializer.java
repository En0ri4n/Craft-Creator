package fr.eno.craftcreator.recipes.serializers;

import cofh.lib.fluid.FluidIngredient;
import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.TreeExtractorMapping;
import cofh.thermal.core.util.recipes.machine.*;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class ThermalRecipeSerializer extends ModRecipeSerializer
{
    private static final ThermalRecipeSerializer INSTANCE = new ThermalRecipeSerializer();

    private ThermalRecipeSerializer()
    {
        super(SupportedMods.THERMAL);
    }

    public void serializeTreeExtractorRecipe(Block trunk, Block leaves, Fluid fluidResult, int fluidAmount)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR);
        recipeObj.addProperty("trunk", Utils.notNull(trunk.getRegistryName()).toString());
        recipeObj.addProperty("leaves", Utils.notNull(leaves.getRegistryName()).toString());
        recipeObj.add("result", mapToJsonObject(ImmutableMap.of("fluid", fluidResult.getRegistryName(), "amount", fluidAmount)));

        addRecipeTo(recipeObj, TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR, fluidResult.getRegistryName());
    }

    public void serializePulverizerRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput outputs, double exp, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_PULVERIZER);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.addProperty("experience", exp);
        addIngredients(input, outputs, recipeObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_PULVERIZER, outputs.getOneOutput().registryName());
    }

    public void serializeSawmillRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput outputs, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_SAWMILL);
        addEnergy(recipeObj, energy, isModEnergy);
        addIngredients(input, outputs, recipeObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_SAWMILL, outputs.getOneOutput().registryName());
    }

    public void serializeSmelterRecipe(List<RecipeEntry.MultiInput> input, RecipeEntry.MultiOutput output, double experience, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_SMELTER);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.addProperty("experience", experience);

        List<RecipeEntry.Input> flatInput = new ArrayList<>();
        input.forEach(recipeMultiInput -> flatInput.addAll(recipeMultiInput.getInputs()));

        if(flatInput.size() == 1)
        {
            JsonObject singleIngredientObj = new JsonObject();
            singleIngredientObj.addProperty(flatInput.get(0).isTag() ? "tag" : "item", flatInput.get(0).registryName().toString());
            singleIngredientObj.addProperty("count", flatInput.get(0).count());
            recipeObj.add("ingredient", singleIngredientObj);
        }
        else
        {
            JsonArray ingredientsObj = new JsonArray();

            for(RecipeEntry.MultiInput ingredients : input)
            {
                JsonObject ingredientObj = new JsonObject();

                if(ingredients.isEmpty()) continue;

                if(ingredients.size() == 1)
                {
                    ingredientObj.addProperty(ingredients.get(0).isTag() ? "tag" : "item", ingredients.get(0).registryName().toString());
                    ingredientObj.addProperty("count", ingredients.get(0).count());
                }
                else
                {
                    JsonArray ingredientValues = new JsonArray();

                    for(RecipeEntry.Input value : ingredients.getInputs())
                    {
                        JsonObject ingredientValueObj = new JsonObject();
                        ingredientValueObj.addProperty(value.isTag() ? "tag" : "item", value.registryName().toString());
                        ingredientValues.add(ingredientValueObj);
                    }

                    ingredientObj.add("value", ingredientValues);
                    ingredientObj.addProperty("count", ingredients.get(0).count());
                }

                ingredientsObj.add(ingredientObj);
            }

            recipeObj.add("ingredients", ingredientsObj);
        }

        recipeObj.add("result", getResultArray(output));

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_SMELTER, output.getOneOutput().registryName());
    }

    public void serializeInsolatorRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput outputs, double waterMod, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_INSOLATOR);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.addProperty("water_mod", waterMod);

        addIngredients(input, outputs, recipeObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_INSOLATOR, outputs.getOneOutput().registryName());
    }

    public void serializePressRecipe(RecipeEntry.Input input, RecipeEntry.Input inputDie, RecipeEntry.Output output, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_PRESS);
        addEnergy(recipeObj, energy, isModEnergy);
        JsonArray ingredientsArray = getInputArray(input);
        if(!inputDie.registryName().equals(Items.AIR.getRegistryName()))
            ingredientsArray.add(singletonItemJsonObject(inputDie));
        recipeObj.add("ingredients", ingredientsArray);
        recipeObj.add("result", mapToJsonObject(ImmutableMap.of("item", output.registryName(), "count", output.count())));

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_PRESS, output.registryName());
    }

    public void serializeCentrifugeRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput outputs, RecipeEntry.FluidOutput fluidOutput, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_CENTRIFUGE);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.add("ingredient", getInput(input));
        JsonArray resultObj = getResultArray(outputs);
        resultObj.add(mapToJsonObject(ImmutableMap.of("fluid", fluidOutput.registryName(), "amount", fluidOutput.getAmount())));
        recipeObj.add("result", resultObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_CENTRIFUGE, outputs.getOneOutput().registryName());
    }

    public void serializeChillerRecipe(RecipeEntry.FluidInput inputFluid, RecipeEntry.Input input, RecipeEntry.Output output, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_CHILLER);
        addEnergy(recipeObj, energy, isModEnergy);
        JsonArray inputArray = getInputArray(input);
        inputArray.add(mapToJsonObject(ImmutableMap.of("fluid", inputFluid.registryName(), "amount", inputFluid.getAmount())));
        recipeObj.add("ingredients", inputArray);
        recipeObj.add("result", getResultArray(output));

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_CHILLER, output.registryName());
    }

    public void serializeCrucibleRecipe(RecipeEntry.Input input, RecipeEntry.FluidOutput output, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_CRUCIBLE);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.add("ingredient", getInput(input));
        recipeObj.add("result", mapToJsonObject(ImmutableMap.of("fluid", output.registryName(), "amount", output.getAmount())));

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_CRUCIBLE, output.registryName());
    }

    public void serializeRefineryRecipe(RecipeEntry.FluidInput inputFluid, RecipeEntry.LuckedOutput outputItem, RecipeEntry.FluidOutput outputFluid, RecipeEntry.FluidOutput secondOutputFluid, Number energy, boolean isModEnergy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_REFINERY);
        addEnergy(recipeObj, energy, isModEnergy);
        recipeObj.add("ingredient", mapToJsonObject(ImmutableMap.of("fluid", inputFluid.registryName(), "amount", inputFluid.getAmount())));
        JsonArray resultObj = getResultArray(outputItem);
        if(outputItem.getItem() != Items.AIR)
            resultObj.add(mapToJsonObject(ImmutableMap.of("item", outputItem.registryName(), "count", outputItem.count(), "chance", outputItem.getChance())));
        if(outputFluid.getFluid() != Fluids.EMPTY)
            resultObj.add(mapToJsonObject(ImmutableMap.of("fluid", outputFluid.registryName(), "amount", outputFluid.getAmount())));
        if(secondOutputFluid.getFluid() != Fluids.EMPTY)
            resultObj.add(mapToJsonObject(ImmutableMap.of("fluid", secondOutputFluid.registryName(), "amount", secondOutputFluid.getAmount())));
        recipeObj.add("result", resultObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_REFINERY, outputItem.registryName());
    }

    public void serializeBottlerRecipe(RecipeEntry.Input input, RecipeEntry.FluidInput inputFluid, RecipeEntry.Output output, Number value, boolean isEnergyMod)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_BOTTLER);
        addEnergy(recipeObj, value, isEnergyMod);
        JsonArray inputArray = getInputArray(input);
        inputArray.add(mapToJsonObject(ImmutableMap.of("fluid", inputFluid.registryName(), "amount", inputFluid.getAmount())));
        recipeObj.add("ingredients", inputArray);
        recipeObj.add("result", getResultArray(output));

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_BOTTLER, output.registryName());
    }

    public void serializePyrolyzerRecipe(RecipeEntry.Input input, RecipeEntry.MultiOutput outputItems, RecipeEntry.FluidOutput outputFluid, double experience, Number energy, boolean isEnergyMod)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_PYROLYZER);
        addEnergy(recipeObj, energy, isEnergyMod);
        recipeObj.addProperty("experience", experience);
        recipeObj.add("ingredient", getInput(input));
        JsonArray resultObj = getResultArray(outputItems);
        if(outputFluid.getFluid() != Fluids.EMPTY)
            resultObj.add(mapToJsonObject(ImmutableMap.of("fluid", outputFluid.registryName(), "amount", outputFluid.getAmount())));
        recipeObj.add("result", resultObj);

        addRecipeTo(recipeObj, TCoreRecipeTypes.RECIPE_PYROLYZER, outputItems.getOneOutput().registryName());
    }
    
    private void addEnergy(JsonObject obj, Number energy, boolean isModEnergy)
    {
        if(isModEnergy)
            obj.addProperty("energy_mod", energy);
        else
            obj.addProperty("energy", energy);
    }

    private void addIngredients(RecipeEntry.Input input, RecipeEntry.MultiOutput outputs, JsonObject obj)
    {
        obj.add("ingredient", singletonItemJsonObject(input));
        JsonArray resultObj = new JsonArray();
        for(RecipeEntry.Output output : outputs.getOutputs())
        {
            JsonObject itemResultObj = new JsonObject();
            itemResultObj.addProperty("item", output.registryName().toString());
            itemResultObj.addProperty("count", output.count());
            if(output instanceof RecipeEntry.LuckedOutput && ((RecipeEntry.LuckedOutput) output).getChance() != 1D) itemResultObj.addProperty("chance", ((RecipeEntry.LuckedOutput) output).getChance());
            resultObj.add(itemResultObj);
        }
        obj.add("result", resultObj);
    }

    @Override
    public CraftIngredients getInput(IRecipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof TreeExtractorMapping)
        {
            TreeExtractorMapping treeExtractorRecipe = (TreeExtractorMapping) recipe;
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(treeExtractorRecipe.getTrunk().getRegistryName(), 1, "Trunk"));
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(treeExtractorRecipe.getLeaves().getRegistryName(), 1, "Leaves"));
        }
        else if(recipe instanceof PulverizerRecipe)
        {
            PulverizerRecipe pulverizerRecipe = (PulverizerRecipe) recipe;
            putIfNotEmpty(inputIngredients, pulverizerRecipe.getInputItems());
        }
        else if(recipe instanceof SawmillRecipe)
        {
            SawmillRecipe sawmillRecipe = (SawmillRecipe) recipe;
            putIfNotEmpty(inputIngredients, sawmillRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, sawmillRecipe.getEnergy(), false));
        }
        else if(recipe instanceof SmelterRecipe)
        {
            SmelterRecipe smelterRecipe = (SmelterRecipe) recipe;
            putIfNotEmpty(inputIngredients, smelterRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, smelterRecipe.getEnergy(), false));
        }
        else if(recipe instanceof InsolatorRecipe)
        {
            InsolatorRecipe insolatorRecipe = (InsolatorRecipe) recipe;
            putIfNotEmpty(inputIngredients, insolatorRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, insolatorRecipe.getEnergy(), false));
        }
        else if(recipe instanceof PressRecipe)
        {
            PressRecipe pressRecipe = (PressRecipe) recipe;
            putIfNotEmpty(inputIngredients, pressRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, pressRecipe.getEnergy(), false));
        }
        else if(recipe instanceof FurnaceRecipe)
        {
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
            putIfNotEmpty(inputIngredients, furnaceRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, furnaceRecipe.getEnergy(), false));
        }
        else if(recipe instanceof CentrifugeRecipe)
        {
            CentrifugeRecipe centrifugeRecipe = (CentrifugeRecipe) recipe;
            putIfNotEmpty(inputIngredients, centrifugeRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, centrifugeRecipe.getEnergy(), false));
        }
        else if(recipe instanceof ChillerRecipe)
        {
            ChillerRecipe chillerRecipe = (ChillerRecipe) recipe;
            putIfNotEmpty(inputIngredients, chillerRecipe.getInputItems());
            for(FluidIngredient fi : chillerRecipe.getInputFluids())
                for(FluidStack fluidIngredient : fi.getFluids())
                    inputIngredients.addIngredient(new CraftIngredients.FluidIngredient(fluidIngredient.getFluid().getRegistryName(), fluidIngredient.getAmount()));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, chillerRecipe.getEnergy(), false));
        }
        else if(recipe instanceof CrucibleRecipe)
        {
            CrucibleRecipe crucibleRecipe = (CrucibleRecipe) recipe;
            putIfNotEmpty(inputIngredients, crucibleRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, crucibleRecipe.getEnergy(), false));
        }
        else if(recipe instanceof BottlerRecipe)
        {
            BottlerRecipe bottlerRecipe = (BottlerRecipe) recipe;
            putIfNotEmpty(inputIngredients, bottlerRecipe.getInputItems());
            for(FluidIngredient fi : bottlerRecipe.getInputFluids())
                for(FluidStack fluidIngredient : fi.getFluids())
                    inputIngredients.addIngredient(new CraftIngredients.FluidIngredient(fluidIngredient.getFluid().getRegistryName(), fluidIngredient.getAmount()));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, bottlerRecipe.getEnergy(), false));
        }
        else if(recipe instanceof PyrolyzerRecipe)
        {
            PyrolyzerRecipe pyrolyzerRecipe = (PyrolyzerRecipe) recipe;
            putIfNotEmpty(inputIngredients, pyrolyzerRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, pyrolyzerRecipe.getEnergy(), false));
        }

        if(inputIngredients.isEmpty() && recipe instanceof ThermalRecipe)
        {
            ThermalRecipe thermalRecipe = (ThermalRecipe) recipe;
            putIfNotEmpty(inputIngredients, thermalRecipe.getInputItems());
        }

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(IRecipe<?> recipe)
    {
        CraftIngredients outputsIngredient = CraftIngredients.create();

        if(recipe instanceof TreeExtractorMapping)
        {
            TreeExtractorMapping treeExtractorRecipe = (TreeExtractorMapping) recipe;
            outputsIngredient.addIngredient(new CraftIngredients.FluidIngredient(treeExtractorRecipe.getFluid().getFluid().getRegistryName(), treeExtractorRecipe.getFluid().getAmount()));
        }
        else if(recipe instanceof PulverizerRecipe)
        {
            PulverizerRecipe pulverizerRecipe = (PulverizerRecipe) recipe;
            putIfNotEmptyLuckedItems(outputsIngredient, pulverizerRecipe.getOutputItems(), pulverizerRecipe.getOutputItemChances(), "Item");
            outputsIngredient.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, pulverizerRecipe.getXp(), true));
        }
        else if(recipe instanceof SawmillRecipe)
        {
            SawmillRecipe sawmillRecipe = (SawmillRecipe) recipe;
            putIfNotEmptyLuckedItems(outputsIngredient, sawmillRecipe.getOutputItems(), sawmillRecipe.getOutputItemChances(), "Item");
        }
        else if(recipe instanceof SmelterRecipe)
        {
            SmelterRecipe smelterRecipe = (SmelterRecipe) recipe;
            putIfNotEmptyLuckedItems(outputsIngredient, smelterRecipe.getOutputItems(), smelterRecipe.getOutputItemChances(), "Item");
            outputsIngredient.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, smelterRecipe.getXp(), true));
        }
        else if(recipe instanceof PressRecipe)
        {
            PressRecipe pressRecipe = (PressRecipe) recipe;
            for(ItemStack is : pressRecipe.getOutputItems())
                outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(is.getItem().getRegistryName(), is.getCount(), "Item"));
        }
        else if(recipe instanceof FurnaceRecipe)
        {
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
            for(ItemStack is : furnaceRecipe.getOutputItems())
                outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(is.getItem().getRegistryName(), is.getCount(), "Item"));
            outputsIngredient.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, furnaceRecipe.getXp(), true));
        }
        else if(recipe instanceof CentrifugeRecipe)
        {
            CentrifugeRecipe centrifugeRecipe = (CentrifugeRecipe) recipe;
            for(ItemStack is : centrifugeRecipe.getOutputItems())
                outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(is.getItem().getRegistryName(), is.getCount(), "Item"));
            for(FluidStack fluidStack : centrifugeRecipe.getOutputFluids())
                outputsIngredient.addIngredient(new CraftIngredients.FluidIngredient(fluidStack.getFluid().getRegistryName(), fluidStack.getAmount()));
        }
        else if(recipe instanceof ChillerRecipe)
        {
            ChillerRecipe chillerRecipe = (ChillerRecipe) recipe;
            putIfNotEmptyLuckedItems(outputsIngredient, chillerRecipe.getOutputItems(), chillerRecipe.getOutputItemChances(), "Item");
        }
        else if(recipe instanceof CrucibleRecipe)
        {
            CrucibleRecipe crucibleRecipe = (CrucibleRecipe) recipe;
            for(FluidStack fluidStack : crucibleRecipe.getOutputFluids())
                    outputsIngredient.addIngredient(new CraftIngredients.FluidIngredient(fluidStack.getFluid().getRegistryName(), fluidStack.getAmount()));
        }
        else if(recipe instanceof BottlerRecipe)
        {
            BottlerRecipe bottlerRecipe = (BottlerRecipe) recipe;
            for(ItemStack stack : bottlerRecipe.getOutputItems())
                    outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(stack.getItem().getRegistryName(), stack.getCount()));
        }
        else if(recipe instanceof PyrolyzerRecipe)
        {
            PyrolyzerRecipe pyrolyzerRecipe = (PyrolyzerRecipe) recipe;
            putIfNotEmptyLuckedItems(outputsIngredient, pyrolyzerRecipe.getOutputItems(), pyrolyzerRecipe.getOutputItemChances(), "Item");
            for(FluidStack fluidStack : pyrolyzerRecipe.getOutputFluids())
                outputsIngredient.addIngredient(new CraftIngredients.FluidIngredient(fluidStack.getFluid().getRegistryName(), fluidStack.getAmount()));
        }

        if(outputsIngredient.isEmpty())
            outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return outputsIngredient;
    }

    public static ThermalRecipeSerializer get()
    {
        return INSTANCE;
    }
}
