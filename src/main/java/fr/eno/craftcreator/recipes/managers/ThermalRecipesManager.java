package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
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
    
    @Override
    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        ThermalRecipeSerializer.get().setSerializerType(serializerType);
        
        switch(recipe)
        {
            case TREE_EXTRACTOR:
                createTreeExtractorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getValue(RecipeInfos.Parameters.RESIN_AMOUNT).intValue());
                break;
            case PULVERIZER:
                createPulverizerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
                break;
            case SAWMILL:
                createSawmillRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case SMELTER:
                createSmelterRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
                break;
            case INSOLATOR:
                createInsolatorRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.WATER_MOD).doubleValue(), recipeInfos);
                break;
            case PRESS:
                createPressRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case CENTRIFUGE:
                createCentrifugeRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case CHILLER:
                createChillerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case CRUCIBLE:
                createCrucibleRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case REFINERY:
                createRefineryRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos);
                break;
            case BOTTLER:
                createBottlerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos);
                break;
            case PYROLYZER:
                createPyrolyzerRecipe(PositionnedSlot.getSlotsFor(recipe.getSlots(), slots), recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS), recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
                break;
        }
    }

    private void createPyrolyzerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PYROLYZER_SLOTS_INPUT.size(), SlotHelper.PYROLYZER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.MultiOutput outputItems = getLuckedOutputs(slots, recipeInfos);

        RecipeEntry.FluidOutput outputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(5)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializePyrolyzerRecipe(input, outputItems, outputFluid, experience, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createBottlerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.BOTTLER_SLOTS_INPUT.size(), SlotHelper.BOTTLER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(1));

        RecipeEntry.Output output = new RecipeEntry.Output(slots.get(2).getItem().getItem().getRegistryName(), slots.get(2).getItem().getCount());

        ThermalRecipeSerializer.get().serializeBottlerRecipe(input, inputFluid, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createRefineryRecipe(List<Slot> slots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.REFINERY_SLOTS_INPUT.size(), SlotHelper.REFINERY_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        RecipeEntry.LuckedOutput outputItem = new RecipeEntry.LuckedOutput(slots.get(1).getItem().getItem().getRegistryName(), slots.get(1).getItem().getCount(), recipeInfos.getValue(RecipeInfos.Parameters.CHANCE).doubleValue());

        RecipeEntry.FluidOutput outputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(2)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_1).intValue());
        RecipeEntry.FluidOutput secondOutputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(3)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_2).intValue());

        ThermalRecipeSerializer.get().serializeRefineryRecipe(inputFluid, outputItem, outputFluid, secondOutputFluid, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createCrucibleRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CRUCIBLE_SLOTS_INPUT.size(), SlotHelper.CRUCIBLE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.FluidOutput output = new RecipeEntry.FluidOutput(getFluid(slots.get(1)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializeCrucibleRecipe(input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createChillerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CHILLER_SLOTS_INPUT.size() - 1, SlotHelper.CHILLER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(1));

        RecipeEntry.Output output = getSingleOutput(slots.get(2));

        ThermalRecipeSerializer.get().serializeChillerRecipe(inputFluid, input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createCentrifugeRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CENTRIFUGE_SLOTS_INPUT.size(), SlotHelper.CENTRIFUGE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.MultiOutput output = getValidOutputs(slots, 1, slots.size() - 2);
        RecipeEntry.FluidOutput fluidOutput = new RecipeEntry.FluidOutput(getFluid(slots.get(slots.size() - 1)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializeCentrifugeRecipe(input, output, fluidOutput, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createPressRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.Input inputDie = getSingleInput(taggedSlots, slots.get(1));
        
        RecipeEntry.Output output = getSingleOutput(slots.get(2));
        
        ThermalRecipeSerializer.get().serializePressRecipe(input, inputDie, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createInsolatorRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double waterMod, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.INSOLATOR_SLOTS_INPUT.size(), SlotHelper.INSOLATOR_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializeInsolatorRecipe(input, output, waterMod, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createSmelterRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
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
                
                if(taggedSlots.containsKey(slot.getSlotIndex())) input.get(k).add(new RecipeEntry.Input(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount()));
                else input.get(k).add(new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount()));
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
        
        ThermalRecipeSerializer.get().serializeSmelterRecipe(input, output, experience, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createTreeExtractorRecipe(List<Slot> slots, int resin_amount)
    {
        if(isSlotsEmpty(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_INPUT.size(), SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size())) return;
        
        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size()).getItem()).getFluid();
        
        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || resin_amount <= 0) return;
        
        ThermalRecipeSerializer.get().serializeTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, resin_amount);
    }
    
    private void createPulverizerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PULVERIZER_SLOTS_INPUT.size(), SlotHelper.PULVERIZER_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializePulverizerRecipe(input, output, experience, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createSawmillRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SAWMILL_SLOTS_INPUT.size(), SlotHelper.SAWMILL_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializeSawmillRecipe(input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
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
            
            if(recipeInfos.contains(key)) outputs.add(new RecipeEntry.LuckedOutput(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount(), recipeInfos.getValue(key).doubleValue()));
            
            luckIndex++;
        }
        
        return outputs;
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }
}
