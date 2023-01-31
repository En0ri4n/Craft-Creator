package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.recipes.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class MinecraftRecipeManager extends BaseRecipesManager
{
    private static final MinecraftRecipeManager INSTANCE = new MinecraftRecipeManager();

    @Override
    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case CRAFTING_TABLE:
                createCraftingTableRecipe(PositionnedSlot.getSlotsFor(SlotHelper.CRAFTING_TABLE_SLOTS, slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getList(RecipeInfos.Parameters.NBT_SLOTS), recipeInfos.getBoolean(RecipeInfos.Parameters.SHAPED), recipeInfos.getBoolean(RecipeInfos.Parameters.KUBEJS_RECIPE));
                break;
            case FURNACE_SMELTING:
            case FURNACE_BLASTING:
            case FURNACE_SMOKING:
            case CAMPFIRE_COOKING:
                createFurnaceRecipe(recipe, PositionnedSlot.getSlotsFor(SlotHelper.FURNACE_SLOTS, slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos.getValue(RecipeInfos.Parameters.COOKING_TIME).intValue(), recipeInfos.getBoolean(RecipeInfos.Parameters.KUBEJS_RECIPE));
                break;
            case SMITHING_TABLE:
                createSmithingTableRecipe(PositionnedSlot.getSlotsFor(SlotHelper.SMITHING_TABLE_SLOTS, slots), recipeInfos.getBoolean(RecipeInfos.Parameters.KUBEJS_RECIPE));
                break;
            case STONECUTTER:
                createStoneCutterRecipe(PositionnedSlot.getSlotsFor(SlotHelper.STONECUTTER_SLOTS, slots), recipeInfos.getBoolean(RecipeInfos.Parameters.KUBEJS_RECIPE));
                break;
        }
    }

    private void createFurnaceRecipe(ModRecipeCreator recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, int cookingTime, boolean isKubeJSRecipe)
    {
        ResourceLocation input = slots.get(0).getStack().getItem().getRegistryName();
        Item output = slots.get(1).getStack().getItem();

        if(taggedSlots.size() > 0 && taggedSlots.get(0) != null)
            input = taggedSlots.get(0);

        MinecraftRecipeSerializer.get().serializeFurnaceRecipe(recipe, input, output, experience, cookingTime, isKubeJSRecipe);
    }

    public void createSmithingTableRecipe(List<Slot> inventory, boolean isKubeJSRecipe)
    {
        ResourceLocation base = inventory.get(0).getStack().getItem().getRegistryName();
        ResourceLocation addition = inventory.get(1).getStack().getItem().getRegistryName();
        Item output = inventory.get(2).getStack().getItem();

        MinecraftRecipeSerializer.get().serializeSmithingRecipe(base, addition, output.getRegistryName(), isKubeJSRecipe);
    }

    public void createStoneCutterRecipe(List<Slot> inventory, boolean isKubeJSRecipe)
    {
        Item input = inventory.get(0).getStack().getItem();
        ItemStack output = inventory.get(1).getStack();

        MinecraftRecipeSerializer.get().serializeStoneCutterRecipe(input.getRegistryName(), output.getItem().getRegistryName(), output.getCount(), isKubeJSRecipe);
    }

    public void createCraftingTableRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> nbtSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        ItemStack output = slots.get(9).getStack();

        MinecraftRecipeSerializer.get().serializeCraftingTableRecipe(output, slots, taggedSlots, nbtSlots, shaped, isKubeJSRecipe);
    }

    public static MinecraftRecipeManager get()
    {
        return INSTANCE;
    }
}
