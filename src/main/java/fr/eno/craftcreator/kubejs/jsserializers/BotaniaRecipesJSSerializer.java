package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemVial;

import java.util.*;

public class BotaniaRecipesJSSerializer extends ModRecipesJSSerializer
{
    private static final BotaniaRecipesJSSerializer INSTANCE = new BotaniaRecipesJSSerializer();

    private BotaniaRecipesJSSerializer()
    {
        super(SupportedMods.BOTANIA);
    }

    public void createInfusionRecipe(Item ingredient, Block catalyst, ItemStack result, int mana)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.MANA_INFUSION_TYPE);
        obj.addProperty("mana", mana);
        if(catalyst != Blocks.AIR)
            obj.add("catalyst", mapToJsonObject(ImmutableMap.of("type", "block", "block", Objects.requireNonNull(catalyst.getRegistryName()).toString())));

        obj.add("input", singletonJsonObject("item", Objects.requireNonNull(ingredient.getRegistryName()).toString()));
        obj.add("output", getResult(result));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.MANA_INFUSION_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, result.getItem().getRegistryName());
    }

    public void createElvenTradeRecipe(List<Item> ingredients, List<Item> results)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.ELVEN_TRADE_TYPE);
        obj.add("ingredients", listWithSingletonItems(ingredients, "item"));
        obj.add("output", listWithSingletonItems(results, "item"));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.ELVEN_TRADE_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, results.get(0).getRegistryName());
    }

    public void createPureDaisyRecipe(Block input, Block output, int time)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.PURE_DAISY_TYPE);
        obj.addProperty("time", time);
        obj.add("input", mapToJsonObject(ImmutableMap.of("type", "block", "block", Objects.requireNonNull(input.getRegistryName()).toString())));
        obj.add("output", singletonJsonObject("name", Objects.requireNonNull(output.getRegistryName()).toString()));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.PURE_DAISY_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, output.getRegistryName());
    }

    public void createBrewRecipe(List<Item> ingredients, Brew brew)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.BREW_TYPE);
        obj.addProperty("brew", Objects.requireNonNull(BotaniaAPI.instance().getBrewRegistry().getKey(brew)).toString());
        obj.add("ingredients", listWithSingletonItems(ingredients, "item"));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.BREW_TYPE);

        sendSuccessMessage(ModRecipeTypes.BREW_TYPE, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
    }

    public void createPetalRecipe(Multimap<ResourceLocation, Boolean> ingredients, ItemStack result)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.PETAL_TYPE);
        obj.add("output", getResult(result));
        obj.add("ingredients", getArray(ingredients));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.PETAL_TYPE);

        sendSuccessMessage(ModRecipeTypes.PETAL_TYPE, result.getItem().getRegistryName());
    }

    public void createRuneRecipe(Multimap<ResourceLocation, Boolean> ingredients, ItemStack result, int mana)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.RUNE_TYPE);
        obj.add("output", getResult(result));
        obj.addProperty("mana", mana);
        obj.add("ingredients", getArray(ingredients));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.RUNE_TYPE);

        sendSuccessMessage(ModRecipeTypes.RUNE_TYPE, result.getItem().getRegistryName());
    }

    public void createTerraPlateRecipe(Multimap<ResourceLocation, Boolean> ingredients, ItemStack result, int mana)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.TERRA_PLATE_TYPE);
        obj.addProperty("mana", mana);
        obj.add("result", getResult(result));
        obj.add("ingredients", getArray(ingredients));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.TERRA_PLATE_TYPE);

        sendSuccessMessage(ModRecipeTypes.TERRA_PLATE_TYPE, result.getItem().getRegistryName());
    }

    @Override
    public PairValue<String, Integer> getParam(Recipe<?> recipe)
    {
        if(recipe instanceof IManaInfusionRecipe manaInfusionRecipe)
            return PairValue.create("Mana", manaInfusionRecipe.getManaToConsume());
        else if(recipe instanceof IRuneAltarRecipe runeAltarRecipe)
            return PairValue.create("Mana", runeAltarRecipe.getManaUsage());
        else if(recipe instanceof ITerraPlateRecipe terraPlateRecipe)
            return PairValue.create("Mana", terraPlateRecipe.getMana());
        else if(recipe instanceof IPureDaisyRecipe pureDaisyRecipe)
            return PairValue.create("Time", pureDaisyRecipe.getTime());

        return null;
    }

    @Override
    public Map<String, ResourceLocation> getOutput(Recipe<?> recipe)
    {
        Map<String, ResourceLocation> locations = new HashMap<>();

        if(recipe instanceof IPureDaisyRecipe recipePureDaisy)
            locations.put("Block", recipePureDaisy.getOutputState().getBlock().getRegistryName());

        else if(recipe instanceof IElvenTradeRecipe recipeElvenTrade)
            recipeElvenTrade.getOutputs().forEach(is -> locations.put("Item", is.getItem().getRegistryName()));

        else if(recipe instanceof IBrewRecipe recipeBrew)
            locations.put("Brew", BotaniaAPI.instance().getBrewRegistry().getKey(recipeBrew.getBrew()));

        return !locations.isEmpty() ? locations : Collections.singletonMap("Item", recipe.getResultItem().getItem().getRegistryName());
    }

    @Override
    public ItemStack getOneOutput(Map.Entry<String, ResourceLocation> entry)
    {
        if("Brew".equals(Objects.requireNonNull(entry).getKey()))
        {
            return ((ItemVial) ModItems.flask).getItemForBrew(BotaniaAPI.instance().getBrewRegistry().get(entry.getValue()), new ItemStack(ModItems.flask));
        }

        return ItemStack.EMPTY;
    }

    public static BotaniaRecipesJSSerializer get()
    {
        return INSTANCE;
    }
}
