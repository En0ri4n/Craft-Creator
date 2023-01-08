package fr.eno.craftcreator.kubejs.jsserializers;

import fr.eno.craftcreator.kubejs.utils.CraftIngredients;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.serializer.CraftingTableRecipeSerializer;
import fr.eno.craftcreator.serializer.FurnaceRecipeSerializer;
import fr.eno.craftcreator.serializer.SmithingTableRecipeSerializer;
import fr.eno.craftcreator.serializer.StoneCutterRecipeSerializer;
import fr.eno.craftcreator.utils.CraftType;
import io.netty.buffer.Unpooled;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinecraftRecipeSerializer extends ModRecipesJSSerializer
{
    private static MinecraftRecipeSerializer INSTANCE;

    private MinecraftRecipeSerializer()
    {
        super(SupportedMods.MINECRAFT);
    }

    public static void createFurnaceRecipe(NonNullList<ItemStack> inventory, CraftType type, String exp, String cookTime, boolean isKubeJSRecipe)
    {
        Item input = inventory.get(0).getItem();
        Item output = inventory.get(1).getItem();
        FurnaceRecipeSerializer recipe = FurnaceRecipeSerializer.create(type, output);

        try
        {
            recipe.setExperience(Double.parseDouble(exp));
        }
        catch(Exception ignored)
        {
        }
        try
        {
            recipe.setCookingTime(Integer.parseInt(cookTime));
        }
        catch(Exception ignored)
        {
        }

        recipe.setIngredient(input);

        recipe.serializeRecipe(isKubeJSRecipe);
    }

    public static void createSmithingTableRecipe(NonNullList<ItemStack> inventory, boolean isKubeJSRecipe)
    {
        ItemStack output = inventory.get(2);

        List<Item> input = Arrays.asList(inventory.get(0).getItem(), inventory.get(1).getItem());

        SmithingTableRecipeSerializer.create(output.getItem()).setIngredient(input).serializeRecipe(isKubeJSRecipe);
    }

    public static void createStoneCutterRecipe(NonNullList<ItemStack> inventory, boolean isKubeJSRecipe)
    {
        ItemStack output = inventory.get(0);
        Item input = inventory.get(1).getItem();

        StoneCutterRecipeSerializer.create(output.getItem(), output.getCount()).setIngredient(input).serializeRecipe(isKubeJSRecipe);
    }

    public static void createCraftingTableRecipe(List<Slot> slots, RecipeInfos infos)
    {
        ItemStack output = slots.get(9).getItem();

        boolean shaped = infos.getBoolean("shaped");
        boolean isKubeJSRecipe = infos.getBoolean("kubejs_recipe");

        CraftingTableRecipeSerializer recipeSerializer = CraftingTableRecipeSerializer.create(shaped ? CraftType.CRAFTING_TABLE_SHAPED : CraftType.CRAFTING_TABLE_SHAPELESS, output.getItem(), output.getCount());

        List<Item> ingredients = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
            ingredients.add(slots.get(i).getItem().getItem());

        recipeSerializer.setIngredients(ingredients, infos.getMap("tagged_slots"));

        recipeSerializer.serializeRecipe(isKubeJSRecipe);
    }

    public void addMinecraftRecipe(String recipeJson, RecipeType<?> recipeType)
    {
        addRecipeToFile(recipeJson, recipeType);
    }

    @Override
    public void sendSuccessMessage(RecipeType<?> type, ResourceLocation result)
    {
        super.sendSuccessMessage(type, result);
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
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(base.getItems()[0].getItem().getRegistryName(), 1));
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(addition.getItems()[0].getItem().getRegistryName(), 1));
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
        return INSTANCE == null ? INSTANCE = new MinecraftRecipeSerializer() : INSTANCE;
    }
}
