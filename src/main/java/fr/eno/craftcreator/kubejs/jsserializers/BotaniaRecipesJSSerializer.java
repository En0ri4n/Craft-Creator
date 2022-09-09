package fr.eno.craftcreator.kubejs.jsserializers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.RecipeFileUtils;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.ModRecipeTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if(isRecipeExists(ModRecipeTypes.MANA_INFUSION_TYPE, result.getItem().getRegistryName())) return;

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
        if(isRecipeExists(ModRecipeTypes.PURE_DAISY_TYPE, output.getRegistryName())) return;

        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.PURE_DAISY_TYPE);
        obj.addProperty("time", time);
        obj.add("input", RecipeFileUtils.mapToJsonObject(ImmutableMap.of("type", "block", "block", input.getRegistryName().toString())));
        obj.add("output", RecipeFileUtils.singletonJsonObject("name", output.getRegistryName().toString()));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.PURE_DAISY_TYPE);

        sendSuccessMessage(ModRecipeTypes.MANA_INFUSION_TYPE, output.getRegistryName());
    }

    public void createBrewRecipe(List<Item> ingredients, Brew brew)
    {
        if(isRecipeExists(ModRecipeTypes.BREW_TYPE, brew.getRegistryName())) return;

        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.BREW_TYPE);
        obj.addProperty("brew", brew.getRegistryName().toString());
        obj.add("ingredients", RecipeFileUtils.listWithSingletonItems(ingredients, "item"));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.BREW_TYPE);

        sendSuccessMessage(ModRecipeTypes.BREW_TYPE, brew.getRegistryName());
    }

    public void createPetalRecipe(Multimap<ResourceLocation, Boolean> ingredients, ItemStack result)
    {
        if(isRecipeExists(ModRecipeTypes.PETAL_TYPE, result.getItem().getRegistryName())) return;

        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.PETAL_TYPE);
        obj.add("output", getResult(result));
        obj.add("ingredients", getArray(ingredients));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.PETAL_TYPE);

        sendSuccessMessage(ModRecipeTypes.PETAL_TYPE, result.getItem().getRegistryName());
    }

    public void createRuneRecipe(Multimap<ResourceLocation, Boolean> ingredients, ItemStack result, int mana)
    {
        if(isRecipeExists(ModRecipeTypes.RUNE_TYPE, result.getItem().getRegistryName())) return;

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
        if(isRecipeExists(ModRecipeTypes.TERRA_PLATE_TYPE, result.getItem().getRegistryName()))
            return;

        JsonObject obj = new JsonObject();
        RecipeFileUtils.setRecipeType(obj, ModRecipeTypes.TERRA_PLATE_TYPE);
        obj.addProperty("mana", mana);
        obj.add("result", getResult(result));
        obj.add("ingredients", getArray(ingredients));

        addRecipeToFile(gson.toJson(obj), ModRecipeTypes.TERRA_PLATE_TYPE);

        sendSuccessMessage(ModRecipeTypes.TERRA_PLATE_TYPE, result.getItem().getRegistryName());
    }

    private JsonArray getArray(Multimap<ResourceLocation, Boolean> ingredients)
    {
        JsonArray array = new JsonArray();
        ingredients.forEach((loc, isTag) -> array.add(RecipeFileUtils.singletonJsonObject(isTag ? "tag" : "item", loc.toString())));
        return array;
    }

    private JsonObject getResult(ItemStack result)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("item", result.getItem().getRegistryName().toString());
        if(result.getCount() > 1)
            map.put("count", result.getCount());

        return RecipeFileUtils.mapToJsonObject(map);
    }

    private boolean isRecipeExists(IRecipeType<?> recipeType, ResourceLocation resultOutput)
    {
        return recipeType == null && resultOutput == null;
    }

    public static BotaniaRecipesJSSerializer get()
    {
        return INSTANCE == null ? INSTANCE = new BotaniaRecipesJSSerializer() : INSTANCE;
    }
}
