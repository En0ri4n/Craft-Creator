package fr.eno.craftcreator.recipes.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseRecipesManager
{
    public abstract void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos param);

    protected ItemStack getValidOutput(List<Slot> slots, int outputCount)
    {
        for(int i = outputCount; i < slots.size(); i++)
        {
            if(slots.get(i).getHasStack())
                return slots.get(i).getStack();
        }

        return ItemStack.EMPTY;
    }

    protected List<Item> getValidList(List<Slot> slots, int start, int end)
    {
        List<Item> list = new ArrayList<>();

        for(int i = start; i < end; i++)
        {
            if(i > slots.size() - 1) break;

            if(slots.get(i).getHasStack()) list.add(slots.get(i).getStack().getItem());
        }

        return list;
    }

    protected List<Item> getValidIngredients(List<Slot> slots)
    {
        List<Item> list = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i).getHasStack()) list.add(slots.get(i).getStack().getItem());
        }

        return list;
    }

    @SuppressWarnings("unused")
    protected List<Slot> getValidSlots(List<Slot> slots)
    {
        List<Slot> list = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i).getHasStack()) list.add(slots.get(i));
        }

        return list;
    }

    protected boolean isSlotsEmpty(List<Slot> slots, int inputSlotsCount, int outputSlotsCount)
    {
        boolean hasNoInput = true;
        boolean hasNoOutput = true;

        for(int i = 0; i < inputSlotsCount; i++)
        {
            if(slots.get(i).getHasStack())
            {
                hasNoInput = false;
                break;
            }
        }

        for(int i = slots.size() - outputSlotsCount; i < slots.size(); i++)
        {
            if(slots.get(i).getHasStack())
            {
                hasNoOutput = false;
                break;
            }
        }

        return hasNoInput || hasNoOutput;
    }

    protected boolean isValid(ItemStack stack)
    {
        return stack != null && !stack.isEmpty();
    }

    protected Multimap<ResourceLocation, Boolean> getValidIngredients(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        Multimap<ResourceLocation, Boolean> map = ArrayListMultimap.create();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(taggedSlots.containsKey(slots.get(i).getSlotIndex()))
            {
                map.put(taggedSlots.get(i), true);
                continue;
            }

            if(isValid(slots.get(i).getStack())) map.put(slots.get(i).getStack().getItem().getRegistryName(), false);
        }

        return map;
    }
}
