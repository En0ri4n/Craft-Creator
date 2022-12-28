package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.eno.craftcreator.screen.utils.ModRecipeCreatorScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseRecipesManager
{
    public abstract void createRecipe(ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param);

    @Nullable
    protected ItemStack getValidOutput(List<Slot> slots)
    {
        if(slots.get(slots.size() - 1).hasItem())
        {
            for(Slot slot : slots)
            {
                if(slot.hasItem()) return slots.get(slots.size() - 1).getItem();
            }
        }

        return null;
    }

    protected List<Item> getValidList(List<Slot> slots, int start, int end)
    {
        List<Item> list = new ArrayList<>();

        for(int i = start; i < end; i++)
        {
            if(i > slots.size() - 1) break;

            if(slots.get(i).hasItem())
                list.add(slots.get(i).getItem().getItem());
        }

        return list;
    }

    protected List<Item> getValidIngredients(List<Slot> slots)
    {
        List<Item> list = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i).hasItem())
                list.add(slots.get(i).getItem().getItem());
        }

        return list;
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
            if(taggedSlots.containsKey(i))
            {
                map.put(taggedSlots.get(i), true);
                continue;
            }

            if(isValid(slots.get(i).getItem()))
                map.put(slots.get(i).getItem().getItem().getRegistryName(), false);
        }

        return map;
    }
}
