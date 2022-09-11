package fr.eno.craftcreator.kubejs.jsserializers;

import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.serializer.CraftingTableRecipeSerializer;
import fr.eno.craftcreator.serializer.FurnaceRecipeSerializer;
import fr.eno.craftcreator.serializer.SmithingTableRecipeSerializer;
import fr.eno.craftcreator.serializer.StoneCutterRecipeSerializer;
import fr.eno.craftcreator.utils.CraftType;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

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

        try { recipe.setExperience(Double.parseDouble(exp)); } catch(Exception ignored) {}
        try { recipe.setCookingTime(Integer.parseInt(cookTime)); } catch(Exception ignored) {}

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

    public static void createCraftingTableRecipe(NonNullList<ItemStack> inventory, Map<SlotItemHandler, ResourceLocation> taggedSlots, boolean isShaped, boolean isKubeJSRecipe)
    {
        ItemStack output = inventory.get(9);

        CraftingTableRecipeSerializer recipe = CraftingTableRecipeSerializer.create(isShaped ? CraftType.CRAFTING_TABLE_SHAPED : CraftType.CRAFTING_TABLE_SHAPELESS, output.getItem(), output.getCount());

        List<Item> ingredients = new ArrayList<>();

        for (int i = 0; i < 9; i++)
            ingredients.add(inventory.get(i).getItem());

        recipe.setIngredients(ingredients, taggedSlots);

        recipe.serializeRecipe(isKubeJSRecipe);
    }

    public void addMinecraftRecipe(String recipeJson, IRecipeType<?> recipeType)
    {
        addRecipeToFile(recipeJson, recipeType);
    }

    @Override
    public PairValue<String, Integer> getParam(IRecipe<?> recipe)
    {
        return null;
    }

    @Override
    public void sendSuccessMessage(IRecipeType<?> type, ResourceLocation result)
    {
        super.sendSuccessMessage(type, result);
    }

    @Override
    public Map<String, ResourceLocation> getOutput(IRecipe<?> recipe)
    {
        return Collections.singletonMap("Item", recipe.getRecipeOutput().getItem().getRegistryName());
    }

    @Override
    public ItemStack getOneOutput(Map.Entry<String, ResourceLocation> entry)
    {
        switch(Objects.requireNonNull(entry).getKey())
        {
            case "Block":
                return new ItemStack(ForgeRegistries.BLOCKS.getValue(entry.getValue()));
            case "Item":
                return new ItemStack(ForgeRegistries.ITEMS.getValue(entry.getValue()));
        }

        return ItemStack.EMPTY;
    }

    public static MinecraftRecipeSerializer get()
    {
        return INSTANCE == null ? INSTANCE = new MinecraftRecipeSerializer() : INSTANCE;
    }
}
