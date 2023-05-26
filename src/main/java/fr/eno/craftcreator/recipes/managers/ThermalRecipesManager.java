package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.ThermalRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class ThermalRecipesManager extends BaseRecipesManager
{
    private static final ThermalRecipesManager INSTANCE = new ThermalRecipesManager();
    
    @Override
    public void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        ThermalRecipeSerializer.get().setSerializerType(serializerType);
        
        List<SlotItemHandler> currentSlots = PositionnedSlot.getSlotsFor(recipe.getSlots(), slots);
        Map<Integer, ResourceLocation> taggedSlots = recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS);
        
        if(recipe.is(TREE_EXTRACTOR))
            createTreeExtractorRecipe(currentSlots, recipeInfos.getValue(RecipeInfos.Parameters.RESIN_AMOUNT).intValue());
        else if(recipe.is(PULVERIZER))
            createPulverizerRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
        else if(recipe.is(SAWMILL))
            createSawmillRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(SMELTER))
            createSmelterRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.EXPERIENCE).doubleValue(), recipeInfos);
        else if(recipe.is(INSOLATOR))
            createInsolatorRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.WATER_MOD).doubleValue(), recipeInfos);
        else if(recipe.is(PRESS))
            createPressRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(CENTRIFUGE))
            createCentrifugeRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(CHILLER))
            createChillerRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(CRUCIBLE))
            createCrucibleRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(REFINERY))
            createRefineryRecipe(currentSlots, recipeInfos);
        else if(recipe.is(BOTTLER))
            createBottlerRecipe(currentSlots, taggedSlots, recipeInfos);
        else if(recipe.is(PYROLYZER))
            createPyrolyzerRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.WATER_MOD).doubleValue(), recipeInfos);
    }

    private void createPyrolyzerRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PYROLYZER_SLOTS_INPUT.size(), SlotHelper.PYROLYZER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.MultiOutput outputItems = getLuckedOutputs(slots, recipeInfos);

        RecipeEntry.FluidOutput outputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(5)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializePyrolyzerRecipe(input, outputItems, outputFluid, experience, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createBottlerRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.BOTTLER_SLOTS_INPUT.size(), SlotHelper.BOTTLER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(1));

        RecipeEntry.Output output = new RecipeEntry.Output(slots.get(2).getItem().getItem().getRegistryName(), slots.get(2).getItem().getCount());

        ThermalRecipeSerializer.get().serializeBottlerRecipe(input, inputFluid, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createRefineryRecipe(List<SlotItemHandler> slots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.REFINERY_SLOTS_INPUT.size(), SlotHelper.REFINERY_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        RecipeEntry.LuckedOutput outputItem = new RecipeEntry.LuckedOutput(slots.get(1).getItem().getItem().getRegistryName(), slots.get(1).getItem().getCount(), recipeInfos.getValue(RecipeInfos.Parameters.CHANCE).doubleValue());

        RecipeEntry.FluidOutput outputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(2)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_1).intValue());
        RecipeEntry.FluidOutput secondOutputFluid = new RecipeEntry.FluidOutput(getFluid(slots.get(3)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_2).intValue());

        ThermalRecipeSerializer.get().serializeRefineryRecipe(inputFluid, outputItem, outputFluid, secondOutputFluid, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createCrucibleRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CRUCIBLE_SLOTS_INPUT.size(), SlotHelper.CRUCIBLE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.FluidOutput output = new RecipeEntry.FluidOutput(getFluid(slots.get(1)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializeCrucibleRecipe(input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createChillerRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CHILLER_SLOTS_INPUT.size() - 1, SlotHelper.CHILLER_SLOTS_OUTPUT.size())) return;

        RecipeEntry.FluidInput inputFluid = new RecipeEntry.FluidInput(getFluid(slots.get(0)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(1));

        RecipeEntry.Output output = getSingleOutput(slots.get(2));

        ThermalRecipeSerializer.get().serializeChillerRecipe(inputFluid, input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createCentrifugeRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.CENTRIFUGE_SLOTS_INPUT.size(), SlotHelper.CENTRIFUGE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));

        RecipeEntry.MultiOutput output = getValidOutputs(slots, 1, slots.size() - 2);
        RecipeEntry.FluidOutput fluidOutput = new RecipeEntry.FluidOutput(getFluid(slots.get(slots.size() - 1)), recipeInfos.getValue(RecipeInfos.Parameters.FLUID_AMOUNT_0).intValue());

        ThermalRecipeSerializer.get().serializeCentrifugeRecipe(input, output, fluidOutput, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }

    private void createPressRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PRESS_SLOTS_INPUT.size(), SlotHelper.PRESS_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.Input inputDie = getSingleInput(taggedSlots, slots.get(1));
        
        RecipeEntry.Output output = getSingleOutput(slots.get(2));
        
        ThermalRecipeSerializer.get().serializePressRecipe(input, inputDie, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createInsolatorRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, double waterMod, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.INSOLATOR_SLOTS_INPUT.size(), SlotHelper.INSOLATOR_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializeInsolatorRecipe(input, output, waterMod, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createSmelterRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
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
    
    private void createTreeExtractorRecipe(List<SlotItemHandler> slots, int resin_amount)
    {
        if(isSlotsEmpty(slots, SlotHelper.TREE_EXTRACTOR_SLOTS_INPUT.size(), SlotHelper.TREE_EXTRACTOR_SLOTS_OUTPUT.size())) return;
        
        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        RecipeEntry.FluidOutput fluidOutput = new RecipeEntry.FluidOutput(getFluid(slots.get(2)), resin_amount);
        
        if(trunk.isEmpty() || leaves.isEmpty() || fluidOutput.getFluid() == Fluids.EMPTY || resin_amount <= 0) return;
        
        ThermalRecipeSerializer.get().serializeTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluidOutput);
    }
    
    private void createPulverizerRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, double experience, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.PULVERIZER_SLOTS_INPUT.size(), SlotHelper.PULVERIZER_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializePulverizerRecipe(input, output, experience, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private void createSawmillRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(isSlotsEmpty(slots, SlotHelper.SAWMILL_SLOTS_INPUT.size(), SlotHelper.SAWMILL_SLOTS_OUTPUT.size())) return;
        
        RecipeEntry.Input input = getSingleInput(taggedSlots, slots.get(0));
        RecipeEntry.MultiOutput output = getLuckedOutputs(slots, recipeInfos);
        
        ThermalRecipeSerializer.get().serializeSawmillRecipe(input, output, recipeInfos.getValue(RecipeInfos.Parameters.ENERGY), recipeInfos.getBoolean(RecipeInfos.Parameters.ENERGY_MOD));
    }
    
    private RecipeEntry.MultiOutput getLuckedOutputs(List<SlotItemHandler> slots, RecipeInfos recipeInfos)
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
