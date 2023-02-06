package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.recipes.serializers.ThermalRecipesSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.utils.PairValues;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
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
        ResourceLocation input = slots.get(0).getItem().getItem().getRegistryName();
        int count = slots.get(0).getItem().getCount();
        ResourceLocation inputDie = slots.get(1).getItem().getItem().getRegistryName();

        if(taggedSlots.containsKey(slots.get(0).getSlotIndex()))
            input = taggedSlots.get(slots.get(0).getSlotIndex());

        ItemStack output = slots.get(2).getItem();

        ThermalRecipesSerializer.get().serializePressRecipe(input, count, inputDie, output, energy);
    }

    private void createInsolatorRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double energyMod, double waterMod, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.INSOLATOR_SLOTS_INPUT.size(), SlotHelper.INSOLATOR_SLOTS_OUTPUT.size()))
            return;

        PairValues<ResourceLocation, Map<ItemStack, Double>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().serializeInsolatorRecipe(values.getFirstValue(), values.getSecondValue(), energyMod, waterMod);
    }

    private void createSmelterRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int energy, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SMELTER_SLOTS_INPUT.size(), SlotHelper.SMELTER_SLOTS_OUTPUT.size())) return;

        List<List<RecipeInput>> input = new ArrayList<>(3);

        Map<ItemStack, Double> output = new HashMap<>(); // Itemstack -> The item | Double -> The chance


        // For inputs slots
        int i = 0;
        for(int k = 0; k < 3; k++)
        {
            List<RecipeInput> tempList = new ArrayList<>(4);
            for(int p = 0; p < 4; p++)
            {
                Slot slot = slots.get(i);
                i++;
                if(!slot.hasItem()) continue;

                if(taggedSlots.containsKey(slot.getSlotIndex()))
                    tempList.add(new RecipeInput(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount()));
                else
                    tempList.add(new RecipeInput(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount()));
            }
            input.add(tempList);
        }

        // For outputs slots
        for(int k = 0; k < SlotHelper.SMELTER_SLOTS_OUTPUT.size(); k++)
        {
            int realIndex = k + SlotHelper.SMELTER_SLOTS_INPUT.size();

            Slot slot = slots.get(realIndex);
            if(!slot.hasItem()) continue;

            output.put(slot.getItem(), recipeInfos.getValue("chance_" + k).doubleValue());
        }

        ThermalRecipesSerializer.get().serializeSmelterRecipe(input, output, energy, experience);
    }

    private void createTreeExtractorRecipe(List<Slot> slots, int resin_amount)
    {
        if(isSlotsEmpty(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_INPUT.size(), SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()))
            return;

        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()).getItem()).getFluid();

        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || resin_amount <= 0) return;

        ThermalRecipesSerializer.get().serializeTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, resin_amount);
    }

    private void createPulverizerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PULVERIZER_SLOTS_INPUT.size(), SlotHelper.PULVERIZER_SLOTS_OUTPUT.size()))
            return;

        PairValues<ResourceLocation, Map<ItemStack, Double>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().serializePulverizerRecipe(values.getFirstValue(), values.getSecondValue(), experience);
    }

    private void createSawmillRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int energy, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SAWMILL_SLOTS_INPUT.size(), SlotHelper.SAWMILL_SLOTS_OUTPUT.size())) return;

        PairValues<ResourceLocation, Map<ItemStack, Double>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().serializeSawmillRecipe(values.getFirstValue(), values.getSecondValue(), energy);
    }

    private PairValues<ResourceLocation, Map<ItemStack, Double>> processRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        ResourceLocation input = slots.get(0).getItem().getItem().getRegistryName();
        Map<ItemStack, Double> outputs = new HashMap<>();

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

        return PairValues.create(input, outputs);
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }

    public static class RecipeInput
    {
        private final boolean isTag;
        private final ResourceLocation registryName;
        private final int count;

        public RecipeInput(boolean isTag, ResourceLocation registryName, int count)
        {
            this.isTag = isTag;
            this.registryName = registryName;
            this.count = count;
        }

        public boolean isTag()
        {
            return isTag;
        }

        public ResourceLocation registryName()
        {
            return registryName;
        }

        public int count()
        {
            return count;
        }
    }
}
