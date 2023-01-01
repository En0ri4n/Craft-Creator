package fr.eno.craftcreator.kubejs.jsserializers;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.TreeExtractorMapping;
import cofh.thermal.core.util.recipes.machine.PulverizerRecipe;
import cofh.thermal.core.util.recipes.machine.SawmillRecipe;
import cofh.thermal.core.util.recipes.machine.SmelterRecipe;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.CraftIngredients;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

public class ThermalRecipesSerializer extends ModRecipesJSSerializer
{
    private static final ThermalRecipesSerializer INSTANCE = new ThermalRecipesSerializer();

    private ThermalRecipesSerializer()
    {
        super(SupportedMods.THERMAL);
    }

    public void createTreeExtractorRecipe(Block trunk, Block leaves, Fluid fluidResult, int fluidAmount)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR);
        obj.addProperty("trunk", trunk.getRegistryName().toString());
        obj.addProperty("leaves", leaves.getRegistryName().toString());
        obj.add("result", mapToJsonObject(ImmutableMap.of("fluid", fluidResult.getRegistryName().toString(), "amount", fluidAmount)));

        addRecipeToFile(gson.toJson(obj), TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR);

        sendSuccessMessage(TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR, fluidResult.getRegistryName());
    }

    public void createPulverizerRecipe(ResourceLocation input, Map<ItemStack, Double> outputs, double exp)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, TCoreRecipeTypes.RECIPE_PULVERIZER);
        obj.addProperty("experience", exp);
        obj.add("ingredient", singletonJsonObject(isItem(input) ? "item" : "tag", input.toString()));
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

        addRecipeToFile(gson.toJson(obj), TCoreRecipeTypes.RECIPE_PULVERIZER);
        sendSuccessMessage(TCoreRecipeTypes.RECIPE_PULVERIZER, outputs.keySet().stream().findFirst().get().getItem().getRegistryName());
    }

    public void createSawmillRecipe(ResourceLocation input, Map<ItemStack, Double> outputs, Double energy)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, TCoreRecipeTypes.RECIPE_SAWMILL);
        obj.addProperty("energy", energy);
        obj.add("ingredient", singletonJsonObject(isItem(input) ? "item" : "tag", input.toString()));
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

        addRecipeToFile(gson.toJson(obj), TCoreRecipeTypes.RECIPE_SAWMILL);
        sendSuccessMessage(TCoreRecipeTypes.RECIPE_SAWMILL, outputs.keySet().stream().findFirst().get().getItem().getRegistryName());
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof TreeExtractorMapping treeExtractorRecipe)
        {
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(treeExtractorRecipe.getTrunk().getRegistryName(), 1, "Trunk"));
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(treeExtractorRecipe.getLeaves().getRegistryName(), 1, "Leaves"));
        }
        else if(recipe instanceof PulverizerRecipe pulverizerRecipe)
        {
            putIfNotEmpty(inputIngredients, pulverizerRecipe.getInputItems());
        }
        else if(recipe instanceof SawmillRecipe sawmillRecipe)
        {
            putIfNotEmpty(inputIngredients, sawmillRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, sawmillRecipe.getEnergy()));
        }
        else if(recipe instanceof SmelterRecipe smelterRecipe)
        {
            putIfNotEmpty(inputIngredients, smelterRecipe.getInputItems());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Energy", CraftIngredients.DataIngredient.DataUnit.ENERGY, smelterRecipe.getEnergy()));
        }

        if(inputIngredients.isEmpty())
            putIfNotEmpty(inputIngredients, recipe.getIngredients());

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients outputsIngredient = CraftIngredients.create();

        if(recipe instanceof TreeExtractorMapping treeExtractorRecipe)
        {
            outputsIngredient.addIngredient(new CraftIngredients.FluidIngredient(treeExtractorRecipe.getFluid().getFluid().getRegistryName(), treeExtractorRecipe.getFluid().getAmount()));
        }
        else if(recipe instanceof PulverizerRecipe pulverizerRecipe)
        {
            putIfNotEmptyLuckedItems(outputsIngredient, pulverizerRecipe.getOutputItems(), pulverizerRecipe.getOutputItemChances(), "Item");
            outputsIngredient.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, pulverizerRecipe.getXp()));
        }
        else if(recipe instanceof SawmillRecipe sawmillRecipe)
        {
            putIfNotEmptyLuckedItems(outputsIngredient, sawmillRecipe.getOutputItems(), sawmillRecipe.getOutputItemChances(), "Item");
        }
        else if(recipe instanceof SmelterRecipe smelterRecipe)
        {
            putIfNotEmptyLuckedItems(outputsIngredient, smelterRecipe.getOutputItems(), smelterRecipe.getOutputItemChances(), "Item");
            outputsIngredient.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, smelterRecipe.getXp()));
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
