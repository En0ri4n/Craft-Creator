package fr.eno.craftcreator.recipes.serializers;


import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.*;
import vazkii.botania.common.crafting.ModRecipeTypes;

import java.util.Objects;

public class BotaniaRecipeSerializer extends ModRecipeSerializer
{
    private static final BotaniaRecipeSerializer INSTANCE = new BotaniaRecipeSerializer();

    protected BotaniaRecipeSerializer()
    {
        super(SupportedMods.BOTANIA);
    }

    public void serializeInfusionRecipe(RecipeEntry.Input ingredient, RecipeEntry.BlockInput catalyst, RecipeEntry.Output result, int mana)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.MANA_INFUSION_TYPE);
        obj.addProperty("mana", mana);
        if(catalyst.getBlock() != Blocks.AIR)
            obj.add("catalyst", mapToJsonObject(ImmutableMap.of("type", "block", "block", Objects.requireNonNull(catalyst.getRegistryName()).toString())));

        obj.add("input", singletonItemJsonObject(ingredient));
        obj.add("output", getResult(result));

        addRecipeTo(obj, ModRecipeTypes.MANA_INFUSION_TYPE, result.getRegistryName());
    }

    public void serializeElvenTradeRecipe(RecipeEntry.MultiInput ingredients, RecipeEntry.MultiOutput results)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.ELVEN_TRADE_TYPE);
        obj.add("ingredients", listWithSingletonItems(ingredients));
        obj.add("output", listWithSingletonItems(results));

        addRecipeTo(obj, ModRecipeTypes.ELVEN_TRADE_TYPE, results.getOneOutput().getRegistryName());
    }

    public void serializePureDaisyRecipe(RecipeEntry.BlockInput input, RecipeEntry.BlockOutput output, int time)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.PURE_DAISY_TYPE);
        obj.addProperty("time", time);
        obj.add("input", mapToJsonObject(ImmutableMap.of("type", "block", "block", input.getRegistryName().toString())));
        obj.add("output", singletonItemJsonObject("name", output.getRegistryName().toString()));

        addRecipeTo(obj, ModRecipeTypes.PURE_DAISY_TYPE, output.getRegistryName());
    }

    public void serializeBrewRecipe(RecipeEntry.MultiInput ingredients, Brew brew)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.BREW_TYPE);
        obj.addProperty("brew", Objects.requireNonNull(BotaniaAPI.instance().getBrewRegistry().getKey(brew)).toString());
        obj.add("ingredients", listWithSingletonItems(ingredients));

        addRecipeTo(obj, ModRecipeTypes.BREW_TYPE, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
    }

    public void serializePetalRecipe(RecipeEntry.MultiInput ingredients, RecipeEntry.Output result)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.PETAL_TYPE);
        obj.add("output", getResult(result));
        obj.add("ingredients", getInputArray(ingredients));

        addRecipeTo(obj, ModRecipeTypes.PETAL_TYPE, result.getRegistryName());
    }

    public void serializeRuneRecipe(RecipeEntry.MultiInput ingredients, RecipeEntry.Output result, int mana)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.RUNE_TYPE);
        obj.add("output", getResult(result));
        obj.addProperty("mana", mana);
        obj.add("ingredients", getInputArray(ingredients));

        addRecipeTo(obj, ModRecipeTypes.RUNE_TYPE, result.getRegistryName());
    }

    public void serializeTerraPlateRecipe(RecipeEntry.MultiInput ingredients, RecipeEntry.Output result, int mana)
    {
        JsonObject obj = createBaseJson(ModRecipeTypes.TERRA_PLATE_TYPE);
        obj.addProperty("mana", mana);
        obj.add("result", getResult(result));
        obj.add("ingredients", getInputArray(ingredients));

        addRecipeTo(obj, ModRecipeTypes.TERRA_PLATE_TYPE, result.getRegistryName());
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof IPureDaisyRecipe)
        {
            IPureDaisyRecipe pureDaisyRecipe = (IPureDaisyRecipe) recipe;
            inputIngredients.addIngredient(new CraftIngredients.BlockIngredient(pureDaisyRecipe.getInput().getDisplayed().get(0).getBlock().getRegistryName()));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Time", CraftIngredients.DataIngredient.DataUnit.TICK, pureDaisyRecipe.getTime(), false));
        }
        else if(recipe instanceof IElvenTradeRecipe)
        {
            IElvenTradeRecipe elvenTradeRecipe = (IElvenTradeRecipe) recipe;
            putIfNotEmpty(inputIngredients, elvenTradeRecipe.getIngredients());
        }
        else if(recipe instanceof IBrewRecipe)
        {
            IBrewRecipe brewRecipe = (IBrewRecipe) recipe;
            putIfNotEmpty(inputIngredients, brewRecipe.getIngredients());
        }
        else if(recipe instanceof IManaInfusionRecipe)
        {
            IManaInfusionRecipe manaInfusionRecipe = (IManaInfusionRecipe) recipe;
            putIfNotEmpty(inputIngredients, manaInfusionRecipe.getIngredients());
            if(manaInfusionRecipe.getRecipeCatalyst() != null)
                inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(manaInfusionRecipe.getRecipeCatalyst().getDisplayedStacks().get(0).getItem().getRegistryName(), 1, "Catalyst"));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", CraftIngredients.DataIngredient.DataUnit.EMPTY, manaInfusionRecipe.getManaToConsume(), false));
        }
        else if(recipe instanceof IPetalRecipe)
        {
            IPetalRecipe petalRecipe = (IPetalRecipe) recipe;
            putIfNotEmpty(inputIngredients, petalRecipe.getIngredients());
        }
        else if(recipe instanceof IRuneAltarRecipe)
        {
            IRuneAltarRecipe runeAltarRecipe = (IRuneAltarRecipe) recipe;
            putIfNotEmpty(inputIngredients, runeAltarRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", CraftIngredients.DataIngredient.DataUnit.EMPTY, runeAltarRecipe.getManaUsage(), false));
        }
        else if(recipe instanceof ITerraPlateRecipe)
        {
            ITerraPlateRecipe terraPlateRecipe = (ITerraPlateRecipe) recipe;
            putIfNotEmpty(inputIngredients, terraPlateRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Mana", CraftIngredients.DataIngredient.DataUnit.EMPTY, terraPlateRecipe.getMana(), false));
        }

        if(inputIngredients.isEmpty())
            putIfNotEmpty(inputIngredients, recipe.getIngredients());

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients outputIngredients = CraftIngredients.create();

        if(recipe instanceof IPureDaisyRecipe)
        {
            IPureDaisyRecipe pureDaisyRecipe = (IPureDaisyRecipe) recipe;
            outputIngredients.addIngredient(new CraftIngredients.BlockIngredient(pureDaisyRecipe.getOutputState().getBlock().getRegistryName()));
        }
        else if(recipe instanceof IElvenTradeRecipe)
        {
            IElvenTradeRecipe elvenTradeRecipe = (IElvenTradeRecipe) recipe;
            elvenTradeRecipe.getOutputs().forEach(is -> outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(is.getItem().getRegistryName(), is.getCount())));
        }
        else if(recipe instanceof IBrewRecipe)
        {
            IBrewRecipe brewRecipe = (IBrewRecipe) recipe;
            outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(BotaniaAPI.instance().getBrewRegistry().getKey(brewRecipe.getBrew()), 1, "Brew"));
        }

        if(outputIngredients.isEmpty())
            outputIngredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return outputIngredients;
    }

    public static BotaniaRecipeSerializer get()
    {
        return INSTANCE;
    }
}
