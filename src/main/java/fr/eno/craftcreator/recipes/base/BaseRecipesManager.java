package fr.eno.craftcreator.recipes.base;

import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.recipes.utils.SpecialRecipeEntry;
import fr.eno.craftcreator.utils.CommonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;
import java.util.Map;

public abstract class BaseRecipesManager
{
    protected BaseRecipesManager() {}

    /**
     * Main method to create a recipe called by the {@link fr.eno.craftcreator.base.RecipeManagerDispatcher}
     * 
     * @param recipe the recipe creator
     * @param slots the slots of the recipe creator
     * @param param the recipe infos
     * @param serializerType the serializer type
     */
    public abstract void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos param, ModRecipeSerializer.SerializerType serializerType);

    /**
     * Get content of the first non-empty slot as a {@link RecipeEntry.Output}<br>
     * 
     * @param slots the slots of the recipe creator
     * @param outputCount the output slots count
     * @return the output or {@link RecipeEntry.Output#EMPTY} if no output found
     */
    protected RecipeEntry.Output getValidOutput(List<SlotItemHandler> slots, int outputCount)
    {
        for(int i = slots.size() - outputCount; i < slots.size(); i++)
        {
            Slot slot = slots.get(i);
            if(slot.hasItem())
                return new RecipeEntry.Output(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
        }

        return RecipeEntry.Output.EMPTY;
    }

    /**
     * Get the content of all non-empty slots between the specified indexes as a {@link RecipeEntry.MultiInput}<br>
     * 
     * @param slots the slots of the recipe creator
     * @param taggedSlots the tagged slots
     * @param start the start index
     * @param end the end index
     * @return the input or an empty {@link RecipeEntry.MultiInput} if no input found
     */
    protected RecipeEntry.MultiInput getValidInputs(List<SlotItemHandler> slots, Map<Integer ,ResourceLocation> taggedSlots, int start, int end)
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

    /**
     * Get the content of all non-empty slots as a {@link RecipeEntry.MultiOutput}<br>
     * 
     * @param slots the slots of the recipe creator
     * @param start the start index
     * @param end the end index
     * @return the output or an empty {@link RecipeEntry.MultiOutput} if no output found
     */
    protected RecipeEntry.MultiOutput getValidOutputs(List<SlotItemHandler> slots, int start, int end)
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

    /**
     * Get the content of all non-empty slots as a {@link RecipeEntry.MultiInput}<br>
     * 
     * @param slots the slots of the recipe creator
     * @return the input or an empty {@link RecipeEntry.MultiInput} if no input found
     */
    protected RecipeEntry.MultiInput getValidIngredients(List<SlotItemHandler> slots)
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

    /**
     * Utility method to check if the specified slots are empty<br>
     *
     * @param slots the slots of the recipe creator
     * @param inputSlotsCount the input slots count
     * @param outputSlotsCount the output slots count
     * @return false if one input and one output are not empty, true otherwise
     */
    protected boolean areSlotsEmpty(List<SlotItemHandler> slots, int inputSlotsCount, int outputSlotsCount)
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

    /**
     * Get the content of the specified input slot as a {@link RecipeEntry.Input}<br>
     *
     * @param taggedSlots the tagged slots
     * @param slot the slot
     * @return the input (can be a tag or an item)
     */
    protected RecipeEntry.Input getSingleInput(Map<Integer, ResourceLocation> taggedSlots, Slot slot)
    {
        if(taggedSlots.containsKey(slot.getSlotIndex()))
            return new RecipeEntry.Input(true, taggedSlots.get(slot.getSlotIndex()), slot.getItem().getCount());
        else
            return new RecipeEntry.Input(false, slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
    }

    /**
     * Get the content of the specified input slot as a {@link RecipeEntry.BlockInput}<br>
     *
     * @param slot the slot
     * @return the input block or a {@link RecipeEntry.BlockInput} with {@link Blocks#AIR} if the slot is empty
     */
    protected RecipeEntry.BlockInput getBlockInput(Slot slot)
    {
        if(slot.hasItem() && slot.getItem().getItem() instanceof BlockItem)
            return new RecipeEntry.BlockInput(Block.byItem(slot.getItem().getItem()).getRegistryName());

        return new RecipeEntry.BlockInput(Blocks.AIR.getRegistryName());
    }

    /**
     * Get the content of the specified output slot as a {@link RecipeEntry.Output}<br>
     * Ensure that the slot is not empty before calling this method<br>
     *
     * @param slot the slot
     * @return the output or an empty {@link RecipeEntry.Output} if the slot is empty
     */
    protected RecipeEntry.Output getSingleOutput(Slot slot)
    {
        return new RecipeEntry.Output(slot.getItem().getItem().getRegistryName(), slot.getItem().getCount());
    }

    /**
     * Get the content of the specified output slot as a {@link RecipeEntry.BlockOutput}<br>
     *
     * @param slot the slot
     * @return the output block or a {@link RecipeEntry.BlockOutput} with {@link Blocks#AIR} if the slot is empty
     */
    protected RecipeEntry.BlockOutput getBlockOutput(Slot slot)
    {
        if(slot.hasItem() && slot.getItem().getItem() instanceof BlockItem)
            return new RecipeEntry.BlockOutput(Block.byItem(slot.getItem().getItem()).getRegistryName());

        return new RecipeEntry.BlockOutput(Blocks.AIR.getRegistryName());
    }

    /**
     * Check if the specified stack is not null and not empty<br>
     * @param stack the stack to check
     * @return true if the stack is not null and not empty, false otherwise
     */
    protected boolean isValid(ItemStack stack)
    {
        return stack != null && !stack.isEmpty();
    }

    /**
     * Get the content of all non-empty input slots as a {@link RecipeEntry.MultiInput}<br>
     * @param slots
     * @param taggedSlots
     * @return
     */
    protected RecipeEntry.MultiInput getValidIngredients(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots)
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

    // FLAT RECIPE CREATORS //

    public RecipeEntry.Input getInput(SpecialRecipeEntry input)
    {
        if(input.isFluid())
            return new RecipeEntry.FluidInput(CommonUtils.getFluid(input.getRegistryName()), input.getCount());
        else
            return new RecipeEntry.Input(input.isTag(), input.getRegistryName(), input.getCount());
    }

    public RecipeEntry.Output getOutput(SpecialRecipeEntry output)
    {
        if(output.isFluid())
            return new RecipeEntry.FluidOutput(CommonUtils.getFluid(output.getRegistryName()), output.getCount());

        if(output.getChance() != 1D)
            return new RecipeEntry.LuckedOutput(output.getRegistryName(), output.getCount(), output.getChance());

        return new RecipeEntry.Output(output.getRegistryName(), output.getCount());
    }

    /**
     * Get the content of all non-empty input entry as a {@link RecipeEntry.MultiInput}<br>
     * @param inputs the inputs
     * @return the multi input
     */
    public RecipeEntry.MultiInput getValidInputs(List<SpecialRecipeEntry> inputs)
    {
        RecipeEntry.MultiInput multiInput = new RecipeEntry.MultiInput();
        inputs.forEach(sre -> multiInput.add(getInput(sre)));
        return multiInput;
    }

    /**
     * Get the content of all non-empty output entry as a {@link RecipeEntry.MultiOutput}<br>
     * @param outputs the outputs
     * @return the multi output
     */
    public RecipeEntry.MultiOutput getValidOutputs(List<SpecialRecipeEntry> outputs)
    {
        RecipeEntry.MultiOutput multiOutput = new RecipeEntry.MultiOutput();
        outputs.forEach(sre -> multiOutput.add(getOutput(sre)));
        return multiOutput;
    }

    /**
     * Check if the specified list of inputs and outputs are empty<br>
     * @param inputs the inputs
     * @param outputs the outputs
     * @return true if the inputs or the outputs are empty, false otherwise
     */
    protected boolean areEmpty(List<SpecialRecipeEntry> inputs, List<SpecialRecipeEntry> outputs)
    {
        boolean hasNoInput = true;
        boolean hasNoOutput = true;

        for(SpecialRecipeEntry input : inputs)
            if(!input.isEmpty())
            {
                hasNoInput = false;
                break;
            }

        for(SpecialRecipeEntry output : outputs)
            if(!output.isEmpty())
            {
                hasNoOutput = false;
                break;
            }

        return hasNoInput || hasNoOutput;
    }
}
