package fr.eno.craftcreator.recipes.serializers;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.TreeExtractorMapping;
import cofh.thermal.core.util.recipes.machine.*;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.recipes.managers.ThermalRecipesManager;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ThermalRecipesSerializer extends ModRecipesJSSerializer
{
    private static final ThermalRecipesSerializer INSTANCE = new ThermalRecipesSerializer();

    private ThermalRecipesSerializer()
    {
        super(SupportedMods.THERMAL);
    }

    public void serializeTreeExtractorRecipe(Block trunk, Block leaves, Fluid fluidResult, int fluidAmount)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR);
        recipeObj.addProperty("trunk", trunk.getRegistryName().toString());
        recipeObj.addProperty("leaves", leaves.getRegistryName().toString());
        recipeObj.add("result", mapToJsonObject(ImmutableMap.of("fluid", fluidResult.getRegistryName().toString(), "amount", fluidAmount)));

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR, fluidResult.getRegistryName());
    }

    public void serializePulverizerRecipe(ResourceLocation input, Map<ItemStack, Double> outputs, double exp)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_PULVERIZER);
        recipeObj.addProperty("experience", exp);
        addIngredients(input, outputs, recipeObj);

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.RECIPE_PULVERIZER, outputs.keySet().stream().findFirst().orElse(ItemStack.EMPTY).getItem().getRegistryName());
    }

    public void serializeSawmillRecipe(ResourceLocation input, Map<ItemStack, Double> outputs, Integer energy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_SAWMILL);
        recipeObj.addProperty("energy", energy);
        addIngredients(input, outputs, recipeObj);

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.RECIPE_SAWMILL, outputs.keySet().stream().findFirst().orElse(ItemStack.EMPTY).getItem().getRegistryName());
    }

    public void serializeSmelterRecipe(List<List<ThermalRecipesManager.RecipeInput>> input, Map<ItemStack, Double> output, int energy, double experience)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_SMELTER);
        recipeObj.addProperty("energy", energy);
        recipeObj.addProperty("experience", experience);

        List<ThermalRecipesManager.RecipeInput> flatInput = input.stream().flatMap(List::stream).collect(Collectors.toList());

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

            for(List<ThermalRecipesManager.RecipeInput> ingredients : input)
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

                    for(ThermalRecipesManager.RecipeInput value : ingredients)
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

        JsonArray resultObj = new JsonArray();

        for(Map.Entry<ItemStack, Double> outputEntry : output.entrySet())
        {
            JsonObject resultValueObj = new JsonObject();
            resultValueObj.addProperty("item", outputEntry.getKey().getItem().getRegistryName().toString());
            resultValueObj.addProperty("count", outputEntry.getKey().getCount());
            resultObj.add(resultValueObj);
        }

        recipeObj.add("result", resultObj);

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.RECIPE_SMELTER, output.keySet().stream().findFirst().orElse(ItemStack.EMPTY).getItem().getRegistryName());
    }

    public void serializeInsolatorRecipe(ResourceLocation firstValue, Map<ItemStack, Double> secondValue, double energyMod, double waterMod)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_INSOLATOR);
        recipeObj.addProperty("energy_mod", energyMod);
        recipeObj.addProperty("water_mod", waterMod);

        addIngredients(firstValue, secondValue, recipeObj);

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.RECIPE_INSOLATOR, secondValue.keySet().stream().findFirst().orElse(ItemStack.EMPTY).getItem().getRegistryName());
    }

    public void serializePressRecipe(ResourceLocation input, int count, ResourceLocation inputDie, ItemStack output, int energy)
    {
        JsonObject recipeObj = createBaseJson(TCoreRecipeTypes.RECIPE_PRESS);
        recipeObj.addProperty("energy", energy);
        JsonArray ingredientsArray = new JsonArray();
        ingredientsArray.add(mapToJsonObject(ImmutableMap.of(isItem(input) ? "item" : "tag", input.toString(), "count", count)));
        ingredientsArray.add(singletonItemJsonObject("item", inputDie));
        recipeObj.add("ingredients", ingredientsArray);
        recipeObj.add("result", mapToJsonObject(ImmutableMap.of("item", output.getItem().getRegistryName().toString(), "count", output.getCount())));

        addRecipeToKubeJS(gson.toJson(recipeObj), TCoreRecipeTypes.RECIPE_PRESS, output.getItem().getRegistryName());
    }

    private void addIngredients(ResourceLocation input, Map<ItemStack, Double> outputs, JsonObject obj)
    {
        obj.add("ingredient", singletonItemJsonObject(isItem(input) ? "item" : "tag", input));
        JsonArray resultObj = new JsonArray();
        for(Map.Entry<ItemStack, Double> output : outputs.entrySet())
        {
            JsonObject itemResultObj = new JsonObject();
            itemResultObj.addProperty("item", output.getKey().getItem().getRegistryName().toString());
            itemResultObj.addProperty("count", output.getKey().getCount());
            if(output.getValue() != 1D) itemResultObj.addProperty("chance", output.getValue());
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

        if(outputsIngredient.isEmpty())
            outputsIngredient.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return outputsIngredient;
    }

    public static ThermalRecipesSerializer get()
    {
        return INSTANCE;
    }
}
