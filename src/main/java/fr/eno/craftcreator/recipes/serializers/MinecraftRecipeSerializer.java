package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.serializer.DatapackHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

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

        if(isKubeJSRecipe) addRecipeToKubeJS(gson.toJson(obj), smeltType.getRecipeType(), output.getRegistryName());
        else DatapackHelper.serializeRecipe(smeltType.getRecipeType(), output.getRegistryName(), obj);
    }

    public void serializeStoneCutterRecipe(ResourceLocation input, ResourceLocation output, int count, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(IRecipeType.STONECUTTING);
        obj.add("ingredient", singletonItemJsonObject("item", input));
        obj.addProperty("result", output.toString());
        obj.addProperty("count", count);

        if(isKubeJSRecipe) addRecipeToKubeJS(gson.toJson(obj), IRecipeType.STONECUTTING, output);
        else DatapackHelper.serializeRecipe(IRecipeType.STONECUTTING, output, obj);
    }

    public void serializeSmithingRecipe(ResourceLocation base, ResourceLocation addition, ResourceLocation output, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(IRecipeType.SMITHING);
        obj.add("base", singletonItemJsonObject("item", base));
        obj.add("addition", singletonItemJsonObject("item", addition));
        obj.add("result", singletonItemJsonObject("item", output));

        if(isKubeJSRecipe) addRecipeToKubeJS(gson.toJson(obj), IRecipeType.SMITHING, output);
        else DatapackHelper.serializeRecipe(IRecipeType.SMITHING, output, obj);
    }

    public void serializeCraftingTableRecipe(ItemStack output, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> nbtSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(IRecipeType.CRAFTING);

        if(shaped)
        {
            obj.addProperty("type", "minecraft:crafting_shaped");
            obj.add("pattern", DatapackHelper.createPatternJson(slots, taggedSlots));
            obj.add("key", DatapackHelper.createSymbolKeys(slots, taggedSlots));
        }
        else
        {
            obj.addProperty("type", "minecraft:crafting_shapeless");
            obj.add("ingredients", DatapackHelper.createShapelessIngredientsJsonArray(slots));
        }

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", output.getItem().getRegistryName().toString());
        resultObj.addProperty("count", output.getCount());
        if(nbtSlots.contains(9))
        {
            resultObj.addProperty("type", "forge:nbt");
            resultObj.addProperty("nbt", slots.get(9).getStack().getTag().toString());
        }
        obj.add("result", resultObj);

        if(isKubeJSRecipe) addRecipeToKubeJS(gson.toJson(obj), IRecipeType.CRAFTING, output.getItem().getRegistryName());
        else DatapackHelper.serializeRecipe(IRecipeType.CRAFTING, output.getItem().getRegistryName(), obj);
    }

    @Override
    public CraftIngredients getInput(IRecipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof SmithingRecipe) // Fields are not accessible so we need to do this :(
        {
            SmithingRecipe smithingRecipe = (SmithingRecipe) recipe;
            PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
            SmithingRecipe.Serializer serializer = (SmithingRecipe.Serializer) smithingRecipe.getSerializer();
            serializer.write(buffer, smithingRecipe);
            Ingredient base = Ingredient.read(buffer);
            Ingredient addition = Ingredient.read(buffer);
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(base.getMatchingStacks()[0].getItem().getRegistryName(), 1, "Base"));
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(addition.getMatchingStacks()[0].getItem().getRegistryName(), 1, "Addition"));
        }
        else if(recipe instanceof AbstractCookingRecipe)
        {
            AbstractCookingRecipe abstractCookingRecipe = (AbstractCookingRecipe) recipe;
            putIfNotEmpty(inputIngredients, abstractCookingRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Cooking Time", CraftIngredients.DataIngredient.DataUnit.TICK, abstractCookingRecipe.getCookTime(), false));
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Experience", CraftIngredients.DataIngredient.DataUnit.EXPERIENCE, abstractCookingRecipe.getExperience(), false));
        }

        if(inputIngredients.isEmpty()) putIfNotEmpty(inputIngredients, recipe.getIngredients());

        return inputIngredients;
    }

    @Override
    public CraftIngredients getOutput(IRecipe<?> recipe)
    {
        CraftIngredients ingredients = CraftIngredients.create();

        if(recipe instanceof ICraftingRecipe)
        {
            ICraftingRecipe craftingRecipe = (ICraftingRecipe) recipe;
            ingredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getRecipeOutput().getItem().getRegistryName(), recipe.getRecipeOutput().getCount()));
            if(craftingRecipe.getRecipeOutput().hasTag())
                ingredients.addIngredient(new CraftIngredients.NBTIngredient(craftingRecipe.getRecipeOutput().getTag()));
        }

        if(ingredients.isEmpty())
            ingredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getRecipeOutput().getItem().getRegistryName(), recipe.getRecipeOutput().getCount()));

        return ingredients;
    }

    public static MinecraftRecipeSerializer get()
    {
        return INSTANCE;
    }
}
