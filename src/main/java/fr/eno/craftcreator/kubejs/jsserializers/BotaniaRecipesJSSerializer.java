package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemVial;

import java.util.*;

public class BotaniaRecipesJSSerializer extends ModRecipesJSSerializer
{
    private static BotaniaRecipesJSSerializer INSTANCE;
    private static final Gson gson = new GsonBuilder().create();

    private BotaniaRecipesJSSerializer()
    {
        super(SupportedMods.BOTANIA);
    }

    public void createInfusionRecipe(Item ingredient, Block catalyst, ItemStack result, int mana)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.MANA_INFUSION_TYPE);
        obj.addProperty("mana", mana);
        if(catalyst != Blocks.AIR) obj.addProperty("catalyst", catalyst.getRegistryName().toString());
        obj.add("input", RecipeFileUtils.singletonJsonObject("item", ingredient.getRegistryName().toString()));
        obj.add("output", getResult(result));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.MANA_INFUSION_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, result.getItem().getRegistryName());
    }

    public void createElvenTradeRecipe(List<Item> ingredients, List<Item> results)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.ELVEN_TRADE_TYPE);
        obj.add("ingredients", RecipeFileUtils.listWithSingletonItems(ingredients, "item"));
        obj.add("output", RecipeFileUtils.listWithSingletonItems(results, "item"));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.ELVEN_TRADE_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, results.get(0).getRegistryName());
    }

    public void createPureDaisyRecipe(Block input, Block output, int time)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.PURE_DAISY_TYPE);
        obj.addProperty("time", time);
        obj.add("input", RecipeFileUtils.mapToJsonObject(ImmutableMap.of("type", "block", "block", Objects.requireNonNull(input.getRegistryName()).toString())));
        obj.add("output", RecipeFileUtils.singletonJsonObject("name", Objects.requireNonNull(output.getRegistryName()).toString()));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.PURE_DAISY_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, output.getRegistryName());
    }

    public void createBrewRecipe(List<Item> ingredients, Brew brew)
    {
        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.BREW_TYPE);
        obj.addProperty("brew", Objects.requireNonNull(brew.getRegistryName()).toString());
        obj.add("ingredients", RecipeFileUtils.listWithSingletonItems(ingredients, "item"));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.BREW_TYPE);

        sendSuccessMessage(ModRecipeTypes.BREW_TYPE, brew.getRegistryName());
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
    public PairValue<String, Integer> getParam(IRecipe<?> recipe)
    {
        if(recipe instanceof IManaInfusionRecipe)
            return PairValue.create("Mana", ((IManaInfusionRecipe) recipe).getManaToConsume());
        else if(recipe instanceof IRuneAltarRecipe)
            return PairValue.create("Mana", ((IRuneAltarRecipe) recipe).getManaUsage());
        else if(recipe instanceof ITerraPlateRecipe)
            return PairValue.create("Mana", ((ITerraPlateRecipe) recipe).getMana());
        else if(recipe instanceof IPureDaisyRecipe)
            return PairValue.create("Time", ((IPureDaisyRecipe) recipe).getTime());

        return null;
    }

    @Override
    public Map<String, ResourceLocation> getOutput(IRecipe<?> recipe)
    {
        Map<String, ResourceLocation> locations = new HashMap<>();

        if(recipe instanceof IPureDaisyRecipe)
        {
            IPureDaisyRecipe recipePureDaisy = (IPureDaisyRecipe) recipe;
            locations.put("Block", recipePureDaisy.getOutputState().getBlock().getRegistryName());
        }

        if(recipe instanceof IElvenTradeRecipe)
        {
            IElvenTradeRecipe recipeElvenTrade = (IElvenTradeRecipe) recipe;
            recipeElvenTrade.getOutputs().forEach(is -> locations.put("Item", is.getItem().getRegistryName()));
        }

        if(recipe instanceof IBrewRecipe)
        {
            IBrewRecipe recipeBrew = (IBrewRecipe) recipe;
            locations.put("Brew", recipeBrew.getBrew().getRegistryName());
        }

        return !locations.isEmpty() ? locations : Collections.singletonMap("Item", recipe.getRecipeOutput().getItem().getRegistryName());
    }

    @Override
    public ItemStack getOneOutput(Map.Entry<String, ResourceLocation> entry)
    {
        switch(Objects.requireNonNull(entry).getKey())
        {
            case "Brew":
                return ((ItemVial) ModItems.flask).getItemForBrew(BotaniaAPI.instance().getBrewRegistry().getOrDefault(entry.getValue()), new ItemStack(ModItems.flask));
        }

        return ItemStack.EMPTY;
    }

    public static BotaniaRecipesJSSerializer get()
    {
        return INSTANCE == null ? INSTANCE = new BotaniaRecipesJSSerializer() : INSTANCE;
    }
}
