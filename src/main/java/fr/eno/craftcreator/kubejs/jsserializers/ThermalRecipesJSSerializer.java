package fr.eno.craftcreator.kubejs.jsserializers;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.recipes.device.TreeExtractorMapping;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThermalRecipesJSSerializer extends ModRecipesJSSerializer
{
    private static final ThermalRecipesJSSerializer INSTANCE = new ThermalRecipesJSSerializer();

    private ThermalRecipesJSSerializer()
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

    @Override
    public PairValue<String, Integer> getParam(Recipe<?> recipe)
    {
        if(recipe instanceof TreeExtractorMapping treeExtractorRecipe)
        {
            return PairValue.create("Fluid Amount", treeExtractorRecipe.getFluid().getAmount());
        }

        return null;
    }

    @Override
    public Map<String, ResourceLocation> getOutput(Recipe<?> recipe)
    {
        Map<String, ResourceLocation> locations = new HashMap<>();

        if(recipe instanceof TreeExtractorMapping treeExtractorRecipe)
        {
            locations.put("Fluid", treeExtractorRecipe.getFluid().getFluid().getRegistryName());
        }

        return !locations.isEmpty() ? locations : Collections.singletonMap("Item", recipe.getResultItem().getItem().getRegistryName());
    }

    @Override
    public ItemStack getOneOutput(Map.Entry<String, ResourceLocation> entry)
    {
        return ItemStack.EMPTY;
    }

    public static ThermalRecipesJSSerializer get()
    {
        return INSTANCE;
    }
}
