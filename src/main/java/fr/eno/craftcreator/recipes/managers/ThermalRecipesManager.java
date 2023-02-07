package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ThermalRecipesManager extends BaseRecipesManager
{
    private static final ThermalRecipesManager INSTANCE = new ThermalRecipesManager();

    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case TREE_EXTRACTOR:
                createTreeExtractorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getValue(RecipeInfos.Parameters.RESIN_AMOUNT).intValue());
                break;
            case PULVERIZER:
                createPulverizerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
                break;
            case SAWMILL:
                createSawmillRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.ENERGY).intValue(), recipeInfos);
                break;
            case SMELTER:
                createSmelterRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.ENERGY).intValue(), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
                break;
            case INSOLATOR:
                createInsolatorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.ENERGY_MOD).doubleValue(), recipeInfos.getValue(RecipeInfos.Parameters.WATER_MOD).doubleValue(), recipeInfos);
                break;
            case PRESS:
                createPressRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.ENERGY).intValue());
                break;
        }
    }

    private void createPressRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int energy)
    {
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.Input inputDie = getSingleInput(taggedSlots, slots.get(1));

        RecipeEntry.Output output = getSingleOutput(slots.get(2));

        ThermalRecipeSerializer.get().serializePressRecipe(input, inputDie, output, energy);
    }

    private void createInsolatorRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double energyMod, double waterMod, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.INSOLATOR_SLOTS_INPUT.size(), SlotHelper.INSOLATOR_SLOTS_OUTPUT.size()))
            return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);

        ThermalRecipeSerializer.get().serializeInsolatorRecipe(input, output, energyMod, waterMod);
    }

    private void createSmelterRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int energy, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SMELTER_SLOTS_INPUT.size(), SlotHelper.SMELTER_SLOTS_OUTPUT.size())) return;

        List<RecipeEntry.MultiInput> input = Arrays.asList(new RecipeEntry.MultiInput(), new RecipeEntry.MultiInput(), new RecipeEntry.MultiInput());

        RecipeEntry.MultiOutput output = new RecipeEntry.MultiOutput();

        // For inputs slots
        int i = 0;
        for(int k = 0; k < 3; k++)
        {
            for(int p = 0; p < 4; p++)
            {
                Slot slot = slots.get(i++);

                if(!slot.hasItem()) continue;

                if(taggedSlots.containsKey(slot.getSlotIndex()))
                    input.get(k).add(new RecipeEntry.Input(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount()));
                else
                    input.get(k).add(new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount()));
            }
        }

        // For outputs slots
        for(int k = 0; k < SlotHelper.SMELTER_SLOTS_OUTPUT.size(); k++)
        {
            int realIndex = k + SlotHelper.SMELTER_SLOTS_INPUT.size();

            Slot slot = slots.get(realIndex);
            if(!slot.hasItem()) continue;

            output.add(new RecipeEntry.LuckedOutput(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount(), recipeInfos.getValue("chance_" + k).doubleValue()));
        }

        ThermalRecipeSerializer.get().serializeSmelterRecipe(input, output, energy, experience);
    }

    private void createTreeExtractorRecipe(List<Slot> slots, int resin_amount)
    {
        if(isSlotsEmpty(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_INPUT.size(), SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()))
            return;

        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()).getItem()).getFluid();

        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || resin_amount <= 0) return;

        ThermalRecipeSerializer.get().serializeTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, resin_amount);
    }

    private void createPulverizerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PULVERIZER_SLOTS_INPUT.size(), SlotHelper.PULVERIZER_SLOTS_OUTPUT.size()))
            return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);

        ThermalRecipeSerializer.get().serializePulverizerRecipe(input, output, experience);
    }

    private void createSawmillRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int energy, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SAWMILL_SLOTS_INPUT.size(), SlotHelper.SAWMILL_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);

        ThermalRecipeSerializer.get().serializeSawmillRecipe(input, output, energy);
    }

    private RecipeEntry.MultiOutput getLuckedOutputs(List<Slot> slots, RecipeInfos recipeInfos)
    {
        RecipeEntry.MultiOutput outputs = new RecipeEntry.MultiOutput();

        int luckIndex = 0;
        for(int i = 1; i < slots.size() - 1; i++) // First slot is the input
        {
            Slot slot = slots.get(i);
            if(!slot.hasItem()) continue;

            String key = "chance_" + luckIndex;

            if(recipeInfos.contains(key))
                outputs.add(new RecipeEntry.LuckedOutput(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount(), recipeInfos.getValue(key).doubleValue()));

            luckIndex++;
        }

        return outputs;
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }


}
