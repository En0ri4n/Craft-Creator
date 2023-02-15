package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MinecraftRecipeManager extends BaseRecipesManager
{
    private static final MinecraftRecipeManager INSTANCE = new MinecraftRecipeManager();

    @Override
    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        MinecraftRecipeSerializer.get().setSerializerType(serializerType);
        
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
        if(isSlotsEmpty(slots, SlotHelper.FURNACE_SLOTS_INPUT.size(), SlotHelper.FURNACE_SLOTS_OUTPUT.size()))
            return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.Output output = getSingleOutput(slots.get(1));

        MinecraftRecipeSerializer.get().serializeFurnaceRecipe(recipe, input, output, experience, cookingTime, isKubeJSRecipe);
    }

    public void createSmithingTableRecipe(List<Slot> slots, boolean isKubeJSRecipe)
    {
        if(isSlotsEmpty(slots, 2, 1))
            return;

        RecipeEntry.Input base = getSingleInput(Collections.emptyMap(), slots.get(0));
        RecipeEntry.Input addition = getSingleInput(Collections.emptyMap(), slots.get(1));

        RecipeEntry.Output output = getSingleOutput(slots.get(2));

        MinecraftRecipeSerializer.get().serializeSmithingRecipe(base, addition, output, isKubeJSRecipe);
    }

    public void createStoneCutterRecipe(List<Slot> slots, boolean isKubeJSRecipe)
    {
        if(isSlotsEmpty(slots, 1, 1))
            return;

        RecipeEntry.Input input = getSingleInput(Collections.emptyMap(), slots.get(0));
        RecipeEntry.Output output = getSingleOutput(slots.get(1));

        MinecraftRecipeSerializer.get().serializeStoneCutterRecipe(input, output, isKubeJSRecipe);
    }

    public void createCraftingTableRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> nbtSlots, boolean shaped, boolean isKubeJSRecipe)
    {
        if(isSlotsEmpty(slots, 9, 1))
            return;

        ItemStack output = slots.get(9).getItem();

        MinecraftRecipeSerializer.get().serializeCraftingTableRecipe(output, slots, taggedSlots, nbtSlots, shaped, isKubeJSRecipe);
    }

    public static MinecraftRecipeManager get()
    {
        return INSTANCE;
    }
}
