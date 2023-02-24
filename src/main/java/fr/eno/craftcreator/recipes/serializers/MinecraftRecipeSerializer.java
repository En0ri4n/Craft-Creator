package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.DatapackHelper;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class MinecraftRecipeSerializer extends ModRecipeSerializer
{
    private static final MinecraftRecipeSerializer INSTANCE = new MinecraftRecipeSerializer();

    private MinecraftRecipeSerializer()
    {
        super(SupportedMods.MINECRAFT);
    }

    public void serializeFurnaceRecipe(RecipeCreator smeltType, RecipeEntry.Input input, RecipeEntry.Output output, double experience, int cookTime, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(smeltType.getRecipeType());
        obj.add("ingredient", singletonItemJsonObject(input));
        obj.addProperty("experience", experience);
        obj.addProperty("cookingtime", cookTime);
        obj.addProperty("result", output.getRegistryName().toString());

        addRecipeTo(obj, smeltType.getRecipeType(), output.getRegistryName());
    }

    public void serializeStoneCutterRecipe(RecipeEntry.Input input, RecipeEntry.Output output, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(IRecipeType.STONECUTTING);
        obj.add("ingredient", singletonItemJsonObject(input));
        obj.addProperty("result", output.getRegistryName().toString());
        obj.addProperty("count", output.count());

        addRecipeTo(obj, IRecipeType.STONECUTTING, output.getRegistryName());
    }

    public void serializeSmithingRecipe(RecipeEntry.Input base, RecipeEntry.Input addition, RecipeEntry.Output output, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(IRecipeType.SMITHING);
        obj.add("base", singletonItemJsonObject(base));
        obj.add("addition", singletonItemJsonObject(addition));
        obj.add("result", singletonItemJsonObject(output));

        addRecipeTo(obj, IRecipeType.SMITHING, output.getRegistryName());
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
            obj.add("ingredients", DatapackHelper.createShapelessIngredientsJsonArray(slots, taggedSlots));
        }

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", output.getItem().getRegistryName().toString());
        resultObj.addProperty("count", output.getCount());
        if(nbtSlots.contains(9))
        {
            resultObj.addProperty("type", "forge:nbt");
            CompoundNBT nbt = slots.get(9).getItem().getTag();
            nbt.remove("display"); // Remove display to avoid issues with the name and lore (no one wants to see a lore like +NBT in the recipe)
            resultObj.addProperty("nbt", escape(nbt.toString(), false));
        }
        obj.add("result", resultObj);

        addRecipeTo(obj, IRecipeType.CRAFTING, output.getItem().getRegistryName());
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
            serializer.toNetwork(buffer, smithingRecipe);
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient addition = Ingredient.fromNetwork(buffer);
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(base.getItems()[0].getItem().getRegistryName(), 1, "Base"));
            inputIngredients.addIngredient(new CraftIngredients.ItemIngredient(addition.getItems()[0].getItem().getRegistryName(), 1, "Addition"));
        }
        else if(recipe instanceof AbstractCookingRecipe)
        {
            AbstractCookingRecipe abstractCookingRecipe = (AbstractCookingRecipe) recipe;
            putIfNotEmpty(inputIngredients, abstractCookingRecipe.getIngredients());
            inputIngredients.addIngredient(new CraftIngredients.DataIngredient("Cooking Time", CraftIngredients.DataIngredient.DataUnit.TICK, abstractCookingRecipe.getCookingTime(), false));
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
            ingredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));
            if(craftingRecipe.getResultItem().hasTag())
                ingredients.addIngredient(new CraftIngredients.NBTIngredient(craftingRecipe.getResultItem().getTag()));
        }

        if(ingredients.isEmpty())
            ingredients.addIngredient(new CraftIngredients.ItemIngredient(recipe.getResultItem().getItem().getRegistryName(), recipe.getResultItem().getCount()));

        return ingredients;
    }

    public static MinecraftRecipeSerializer get()
    {
        return INSTANCE;
    }
}
