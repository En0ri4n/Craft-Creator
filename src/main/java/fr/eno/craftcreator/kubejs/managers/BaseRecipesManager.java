package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
            if(slots.get(i).hasItem())
                return slots.get(i).getItem();
        }

        return ItemStack.EMPTY;
    }

    protected boolean hasEmptyOutput(List<Slot> slots, int outputCount)
    {
        for(int i = outputCount; i < slots.size(); i++)
        {
            if(slots.get(i).hasItem())
                return false;
        }

        return true;
    }

    protected List<Item> getValidList(List<Slot> slots, int start, int end)
    {
        List<Item> list = new ArrayList<>();

        for(int i = start; i < end; i++)
        {
            if(i > slots.size() - 1) break;

            if(slots.get(i).hasItem()) list.add(slots.get(i).getItem().getItem());
        }

        return list;
    }

    protected List<Item> getValidIngredients(List<Slot> slots)
    {
        List<Item> list = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i).hasItem()) list.add(slots.get(i).getItem().getItem());
        }

        return list;
    }

    protected boolean isSlotsEmpty(List<Slot> slots, int inputSlotsCount, int outputSlotsCount)
    {
        boolean hasInput = true;
        boolean hasOutput = true;

        for(int i = 0; i < inputSlotsCount; i++)
        {
            if(slots.get(i).hasItem())
            {
                hasInput = false;
                break;
            }
        }

        for(int i = slots.size() - outputSlotsCount; i < slots.size(); i++)
        {
            if(slots.get(i).hasItem())
            {
                hasOutput = false;
                break;
            }
        }

        return hasInput || hasOutput;
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

            if(isValid(slots.get(i).getItem())) map.put(slots.get(i).getItem().getItem().getRegistryName(), false);
        }

        return map;
    }
}
