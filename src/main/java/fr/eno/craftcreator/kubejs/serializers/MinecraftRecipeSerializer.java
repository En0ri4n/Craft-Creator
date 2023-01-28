package fr.eno.craftcreator.kubejs.serializers;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.kubejs.utils.CraftIngredients;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.serializer.DatapackHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.List;
import java.util.Map;

public class MinecraftRecipeSerializer extends ModRecipesJSSerializer
{
    private static final MinecraftRecipeSerializer INSTANCE = new MinecraftRecipeSerializer();

    private MinecraftRecipeSerializer()
    {
        super(SupportedMods.MINECRAFT);
    }

    public void serializeFurnaceRecipe(ModRecipeCreator smeltType, ResourceLocation input, Item output, double experience, int cookTime, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(smeltType.getRecipeType());
        obj.add("ingredient", singletonItemJsonObject(input));
        obj.addProperty("experience", experience);
        obj.addProperty("cookingtime", cookTime);
        obj.addProperty("result", output.toString());

        if(isKubeJSRecipe)
            addRecipeToFile(gson.toJson(obj), smeltType.getRecipeType(), output.getRegistryName());
        else
            DatapackHelper.serializeRecipe(smeltType.getRecipeType(), output.getRegistryName(), obj);
    }

    public void serializeStoneCutterRecipe(ResourceLocation input, ResourceLocation output, int count, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(RecipeType.STONECUTTING);
        obj.add("ingredient", singletonItemJsonObject("item", input));
        obj.addProperty("result", output.toString());
        obj.addProperty("count", count);

        if(isKubeJSRecipe)
            addRecipeToFile(gson.toJson(obj), RecipeType.STONECUTTING, output);
        else
            DatapackHelper.serializeRecipe(RecipeType.STONECUTTING, output, obj);
    }

    public void serializeSmithingRecipe(ResourceLocation base, ResourceLocation addition, ResourceLocation output, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(RecipeType.SMITHING);
        obj.add("base", singletonItemJsonObject("item", base));
        obj.add("addition", singletonItemJsonObject("item", addition));
        obj.addProperty("result", output.toString());

        if(isKubeJSRecipe)
            addRecipeToFile(gson.toJson(obj), RecipeType.SMITHING, output);
        else
            DatapackHelper.serializeRecipe(RecipeType.SMITHING, output, obj);
    }

    public void serializeCraftingTableRecipe(ItemStack output, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(RecipeType.CRAFTING);

        if(shaped)
        {
            obj.add("pattern", DatapackHelper.createPatternJson(slots, taggedSlots));
            obj.add("key", DatapackHelper.createSymbolKeys(slots, taggedSlots));
        }
        else
        {
            obj.add("ingredients", DatapackHelper.createShapelessIngredientsJsonArray(slots));
        }

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", output.getItem().getRegistryName().toString());
        resultObj.addProperty("count", output.getCount());
        // TODO Add nbt support for output
        obj.add("result", resultObj);

        if(isKubeJSRecipe)
            addRecipeToFile(gson.toJson(obj), RecipeType.CRAFTING, output.getItem().getRegistryName());
        else
            DatapackHelper.serializeRecipe(RecipeType.CRAFTING, output.getItem().getRegistryName(), obj);
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof UpgradeRecipe smithRecipe) // Fields are not accessible so we need to do this :(
        {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            UpgradeRecipe.Serializer serializer = (UpgradeRecipe.Serializer) smithRecipe.getSerializer();
            serializer.toNetwork(buffer, smithRecipe);
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(base.getItems()[0].getItem().getRegistryName(), 1, "Base"));
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(addition.getItems()[0].getItem().getRegistryName(), 1, "Addition"));
        }
        else if(recipe instanceof AbstractCookingRecipe abstractCookingRecipe)
        {
            putIfNotEmpty(inputIngredients, abstractCookingRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Cooking Time", CraftIngredients.DataIngredient.DataUnit.TICK, abstractCookingRecipe.getCookingTime()));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, abstractCookingRecipe.getExperience()));
        }

        if(inputIngredients.isEmpty()) putIfNotEmpty(inputIngredients, recipe.getIngredients());

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients ingredients = CraftIngredients.create();
        ingredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return ingredients;
    }

    public static MinecraftRecipeSerializer get()
    {
        return INSTANCE;
    }
}
