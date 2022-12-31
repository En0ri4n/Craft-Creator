package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.CraftIngredients;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
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
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunLensRecipe;

import java.util.List;
import java.util.Objects;

public class BotaniaRecipesSerializer extends ModRecipesJSSerializer
{
    private static final BotaniaRecipesSerializer INSTANCE = new BotaniaRecipesSerializer();

    private BotaniaRecipesSerializer()
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
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof IPureDaisyRecipe pureDaisyRecipe)
        {
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(pureDaisyRecipe.getInput().getDisplayed().get(0).getBlock().getRegistryName()));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Time", pureDaisyRecipe.getTime()));
        }
        else if(recipe instanceof IElvenTradeRecipe elvenTradeRecipe)
        {
            putIfNotEmpty(inputIngredients, elvenTradeRecipe.getIngredients());
        }
        else if(recipe instanceof IBrewRecipe brewRecipe)
        {
            putIfNotEmpty(inputIngredients, brewRecipe.getIngredients());
        }
        else if(recipe instanceof IManaInfusionRecipe manaInfusionRecipe)
        {
            putIfNotEmpty(inputIngredients, manaInfusionRecipe.getIngredients());
            if(manaInfusionRecipe.getRecipeCatalyst() != null)
                inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(manaInfusionRecipe.getRecipeCatalyst().getDisplayedStacks().get(0).getItem().getRegistryName(), 1, "Catalyst"));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", manaInfusionRecipe.getManaToConsume()));
        }
        else if(recipe instanceof IPetalRecipe petalRecipe)
        {
            putIfNotEmpty(inputIngredients, petalRecipe.getIngredients());
        }
        else if(recipe instanceof IRuneAltarRecipe runeAltarRecipe)
        {
            putIfNotEmpty(inputIngredients, runeAltarRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", runeAltarRecipe.getManaUsage()));
        }
        else if(recipe instanceof ITerraPlateRecipe terraPlateRecipe)
        {
            putIfNotEmpty(inputIngredients, terraPlateRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", terraPlateRecipe.getMana()));
        }

        if(inputIngredients.isEmpty())
            putIfNotEmpty(inputIngredients, recipe.getIngredients());

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients outputIngredients = CraftIngredients.create();

        if(recipe instanceof IPureDaisyRecipe recipePureDaisy)
        {
            outputIngredients.addIngredient(new CraftIngredients.BlockIngredient(recipePureDaisy.getOutputState().getBlock().getRegistryName()));
        }
        else if(recipe instanceof IElvenTradeRecipe recipeElvenTrade)
        {
            recipeElvenTrade.getOutputs().forEach(is -> outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(is.getItem().getRegistryName(), is.getCount())));
        }
        else if(recipe instanceof IBrewRecipe recipeBrew)
        {
            outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(BotaniaAPI.instance().getBrewRegistry().getKey(recipeBrew.getBrew()), 1, "Brew"));
        }

        if(outputIngredients.isEmpty())
            outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return outputIngredients;
    }

    public static BotaniaRecipesSerializer get()
    {
        return INSTANCE;
    }
}
