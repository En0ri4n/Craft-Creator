package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.jsserializers.ThermalRecipesSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThermalRecipesManager extends BaseRecipesManager
{
    private static final ThermalRecipesManager INSTANCE = new ThermalRecipesManager();

    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case TREE_EXTRACTOR -> createTreeExtractorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getValue("resin_amount").intValue());
            case PULVERIZER -> createPulverizerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap("tagged_slots"), recipeInfos);
            case SAWMILL -> createSawmillRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap("tagged_slots"), recipeInfos);
            case SMELTER -> createSmelterRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap("tagged_slots"), recipeInfos);
            case INSOLATOR -> createInsolatorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap("tagged_slots"), recipeInfos);
        }
    }

    private void createInsolatorRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {

    }

    private void createSmelterRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        ItemStack input = slots.get(0).getItem();
        ItemStack output = slots.get(1).getItem();
        ItemStack secondaryOutput = slots.get(2).getItem();
        int energy = recipeInfos.getValue("energy").intValue();
        double experience = recipeInfos.getValue("experience").doubleValue();
        double chance = recipeInfos.getValue("chance_0").doubleValue();

        if (input.isEmpty() || output.isEmpty())
            return;

        if (secondaryOutput.isEmpty())
        {
            // ThermalRecipesSerializer.get().createSmelterRecipe(input, output, energy, experience);
        }
    }

    private void createTreeExtractorRecipe(List<Slot> slots, int resin_amount)
    {
        if(hasEmptyOutput(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()))
            return;

        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()).getItem()).getFluid();

        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || resin_amount <= 0)
            return;

        ThermalRecipesSerializer.get().createTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, resin_amount);
    }

    private void createPulverizerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PULVERIZER_SLOTS_INPUT.size(), SlotHelper.PULVERIZER_SLOTS_OUTPUT.size()))
            return;

        PairValues<ResourceLocation, PairValues<Map<ItemStack, Double>, Integer>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().createPulverizerRecipe(values.getFirstValue(), values.getSecondValue().getFirstValue(), values.getSecondValue().getSecondValue());
    }

    private void createSawmillRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SAWMILL_SLOTS_INPUT.size(), SlotHelper.SAWMILL_SLOTS_OUTPUT.size()))
            return;

        PairValues<ResourceLocation, PairValues<Map<ItemStack, Double>, Integer>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().createSawmillRecipe(values.getFirstValue(), values.getSecondValue().getFirstValue(), values.getSecondValue().getSecondValue());
    }

    private static PairValues<ResourceLocation, PairValues<Map<ItemStack, Double>, Integer>> processRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        ResourceLocation input = slots.get(0).getItem().getItem().getRegistryName();
        Map<ItemStack, Double> outputs = new HashMap<>();
        int energy = recipeInfos.contains("energy") ? recipeInfos.getValue("energy").intValue() : 0;
        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i + 1).hasItem() && recipeInfos.contains("chance_" + i))
                outputs.put(slots.get(i + 1).getItem(), recipeInfos.getValue("chance_" + i).doubleValue());
        }

        if(!taggedSlots.isEmpty())
        {
            for(Map.Entry<Integer, ResourceLocation> taggedSlot : taggedSlots.entrySet())
            {
                if(taggedSlot.getKey() == slots.get(0).getSlotIndex())
                {
                    input = taggedSlot.getValue();
                    break;
                }
            }
        }

        return PairValues.create(input, PairValues.create(outputs, energy));
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }
}
