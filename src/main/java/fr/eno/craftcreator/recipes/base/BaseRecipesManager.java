package fr.eno.craftcreator.recipes.base;

import fr.eno.craftcreator.base.ModRecipeCreator;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public abstract class BaseRecipesManager
{
    public abstract void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos param, ModRecipeSerializer.SerializerType serializerType);

    protected RecipeEntry.Output getValidOutput(List<Slot> slots, int outputCount)
    {
        for(int i = outputCount; i < slots.size(); i++)
        {
            Slot slot = slots.get(i);
            if(slot.hasItem())
                return new RecipeEntry.Output(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
        }

        return RecipeEntry.Output.EMPTY;
    }

    protected RecipeEntry.MultiInput getValidInputs(List<Slot> slots, Map<Integer ,ResourceLocation> taggedSlots, int start, int end)
    {
        RecipeEntry.MultiInput input = new RecipeEntry.MultiInput();

        for(int i = start; i < end; i++)
        {
            if(i > slots.size() - 1) break;

            Slot slot = slots.get(i);

            if(!slot.hasItem())
                continue;

            input.add(getSingleInput(taggedSlots, slot));
        }

        return input;
    }

    protected RecipeEntry.MultiOutput getValidOutputs(List<Slot> slots, int start, int end)
    {
        RecipeEntry.MultiOutput output = new RecipeEntry.MultiOutput();

        for(int i = start; i < end; i++)
        {
            if(i > slots.size() - 1) break;

            Slot slot = slots.get(i);

            if(!slot.hasItem())
                continue;

            output.add(getSingleOutput(slot));
        }

        return output;
    }

    protected RecipeEntry.MultiInput getValidIngredients(List<Slot> slots)
    {
        RecipeEntry.MultiInput recipeMultiInput = new RecipeEntry.MultiInput();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            Slot slot = slots.get(i);
            if(slot.hasItem())
                recipeMultiInput.add(new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount()));
        }

        return recipeMultiInput;
    }

    protected boolean isSlotsEmpty(List<Slot> slots, int inputSlotsCount, int outputSlotsCount)
    {
        boolean hasNoInput = true;
        boolean hasNoOutput = true;

        for(int i = 0; i < inputSlotsCount; i++)
            if(slots.get(i).hasItem())
            {
                hasNoInput = false;
                break;
            }

        for(int i = slots.size() - outputSlotsCount; i < slots.size(); i++)
            if(slots.get(i).hasItem())
            {
                hasNoOutput = false;
                break;
            }

        return hasNoInput || hasNoOutput;
    }

    protected RecipeEntry.Input getSingleInput(Map<Integer, ResourceLocation> taggedSlots, Slot slot)
    {
        if(taggedSlots.containsKey(slot.getSlotIndex()))
            return new RecipeEntry.Input(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount());
        else
            return new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
    }

    protected RecipeEntry.BlockInput getBlockInput(Slot slot)
    {
        if(slot.hasItem() && slot.getItem().getItem() instanceof BlockItem)
            return new RecipeEntry.BlockInput(Block.byItem(slot.getItem().getItem()).getRegistryName());

        return new RecipeEntry.BlockInput(Blocks.AIR.getRegistryName());
    }

    protected RecipeEntry.Output getSingleOutput(Slot slot)
    {
        return new RecipeEntry.Output(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
    }

    protected RecipeEntry.BlockOutput getBlockOutput(Slot slot)
    {
        if(slot.hasItem() && slot.getItem().getItem() instanceof BlockItem)
            return new RecipeEntry.BlockOutput(Block.byItem(slot.getItem().getItem()).getRegistryName());

        return new RecipeEntry.BlockOutput(Blocks.AIR.getRegistryName());
    }

    protected boolean isValid(ItemStack stack)
    {
        return stack != null && !stack.isEmpty();
    }

    protected RecipeEntry.MultiInput getValidIngredients(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        RecipeEntry.MultiInput recipeMultiInput = new RecipeEntry.MultiInput();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            Slot slot = slots.get(i);

            if(taggedSlots.containsKey(slot.getSlotIndex()))
            {
                recipeMultiInput.add(new RecipeEntry.Input(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount()));
                continue;
            }

            if(isValid(slots.get(i).getItem()))
                recipeMultiInput.add(new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount()));
        }

        return recipeMultiInput;
    }

    protected Fluid getFluid(Slot slot)
    {
    	if(slot.hasItem() && slot.getItem().getItem() instanceof BucketItem)
    	{
    		BucketItem bucket = (BucketItem) slot.getItem().getItem();
            return bucket.getFluid();
    	}

    	return Fluids.EMPTY;
    }
}