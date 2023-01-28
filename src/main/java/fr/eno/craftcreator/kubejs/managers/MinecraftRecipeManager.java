package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
            case CRAFTING_TABLE -> createCraftingTableRecipe(PositionnedSlot.getSlotsFor(SlotHelper.CRAFTING_TABLE_SLOTS, slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getBoolean(RecipeInfos.Parameters.SHAPED), recipeInfos.getBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE));
            case FURNACE_SMELTING, FURNACE_BLASTING, FURNACE_SMOKING, CAMPFIRE_COOKING -> createFurnaceRecipe(recipe, PositionnedSlot.getSlotsFor(SlotHelper.FURNACE_SLOTS, slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos.getValue(RecipeInfos.Parameters.COOKING_TIME).intValue(), recipeInfos.getBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE));
            case SMITHING_TABLE -> createSmithingTableRecipe(PositionnedSlot.getSlotsFor(SlotHelper.SMITHING_TABLE_SLOTS, slots), recipeInfos.getBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE));
            case STONECUTTER -> createStoneCutterRecipe(PositionnedSlot.getSlotsFor(SlotHelper.STONECUTTER_SLOTS, slots), recipeInfos.getBoolean(RecipeInfos.Parameters.IS_KUBEJS_RECIPE));
        }
    }

    private void createFurnaceRecipe(ModRecipeCreator recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, int cookingTime, boolean isKubeJSRecipe)
    {
        ResourceLocation input = slots.get(0).getItem().getItem().getRegistryName();
        Item output = slots.get(1).getItem().getItem();

        if(taggedSlots.size() > 0 && taggedSlots.get(0) != null)
            input = taggedSlots.get(0);

        MinecraftRecipeSerializer.get().serializeFurnaceRecipe(recipe, input, output, experience, cookingTime, isKubeJSRecipe);
    }

    public void createSmithingTableRecipe(List<Slot> inventory, boolean isKubeJSRecipe)
    {
        ResourceLocation base = inventory.get(0).getItem().getItem().getRegistryName();
        ResourceLocation addition = inventory.get(1).getItem().getItem().getRegistryName();
        Item output = inventory.get(2).getItem().getItem();

        MinecraftRecipeSerializer.get().serializeSmithingRecipe(base, addition, output.getRegistryName(), isKubeJSRecipe);
    }

    public void createStoneCutterRecipe(List<Slot> inventory, boolean isKubeJSRecipe)
    {
        Item input = inventory.get(0).getItem().getItem();
        ItemStack output = inventory.get(1).getItem();

        MinecraftRecipeSerializer.get().serializeStoneCutterRecipe(input.getRegistryName(), output.getItem().getRegistryName(), output.getCount(), isKubeJSRecipe);
    }

    public void createCraftingTableRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        ItemStack output = slots.get(9).getItem();

        MinecraftRecipeSerializer.get().serializeCraftingTableRecipe(output, slots, taggedSlots, shaped, isKubeJSRecipe);
    }

    public static MinecraftRecipeManager get()
    {
        return INSTANCE;
    }
}
