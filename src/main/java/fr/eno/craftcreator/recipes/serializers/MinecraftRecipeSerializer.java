package fr.eno.craftcreator.recipes.serializers;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.CraftIngredients;
import fr.eno.craftcreator.recipes.utils.DatapackHelper;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

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
        JsonObject obj = createBaseJson(RecipeType.STONECUTTING);
        obj.add("ingredient", singletonItemJsonObject(input));
        obj.addProperty("result", output.getRegistryName().toString());
        obj.addProperty("count", output.count());

        addRecipeTo(obj, RecipeType.STONECUTTING, output.getRegistryName());
    }

    public void serializeSmithingRecipe(RecipeEntry.Input base, RecipeEntry.Input addition, RecipeEntry.Output output, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(RecipeType.SMITHING);
        obj.add("base", singletonItemJsonObject(base));
        obj.add("addition", singletonItemJsonObject(addition));
        obj.add("result", singletonItemJsonObject(output));

        addRecipeTo(obj, RecipeType.SMITHING, output.getRegistryName());
    }

    public void serializeCraftingTableRecipe(ItemStack output, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> nbtSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        JsonObject obj = createBaseJson(RecipeType.CRAFTING);

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
            CompoundTag nbt = slots.get(9).getItem().getTag();
            nbt.remove("display"); // Remove display to avoid issues with the name and lore (no one wants to see a lore like +NBT in the recipe)
            resultObj.addProperty("nbt", escape(nbt.toString(), false));
        }
        obj.add("result", resultObj);

        addRecipeTo(obj, RecipeType.CRAFTING, output.getItem().getRegistryName());
    }

    @Override
    public CraftIngredients getInput(Recipe<?> recipe)
    {
        CraftIngredients inputIngredients = CraftIngredients.create();

        if(recipe instanceof UpgradeRecipe) // Fields are not accessible so we need to do this :(
        {
            UpgradeRecipe smithingRecipe = (UpgradeRecipe) recipe;
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            UpgradeRecipe.Serializer serializer = (UpgradeRecipe.Serializer) smithingRecipe.getSerializer();
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
    public CraftIngredients getOutput(Recipe<?> recipe)
    {
        CraftIngredients ingredients = CraftIngredients.create();

        if(recipe instanceof CraftingRecipe)
        {
            CraftingRecipe craftingRecipe = (CraftingRecipe) recipe;
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
