package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesJSSerializer;
import fr.eno.craftcreator.screen.BotaniaRecipeCreatorScreen;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotaniaRecipeManagers
{
    public static void createRecipe(BotaniaRecipeCreatorScreen.BotaniaRecipes recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, Object param)
    {
        switch(recipe)
        {
            case MANA_INFUSION:
                createManaInfusionRecipe(PositionnedSlot.getValidSlots(SlotHelper.MANA_INFUSION_SLOTS, slots), param);
                break;
            case ELVEN_TRADE:
                createElvenTradeRecipe(PositionnedSlot.getValidSlots(SlotHelper.ELVEN_TRADE_SLOTS, slots), param);
                break;
            case PURE_DAISY:
                createPureDaisyRecipe(PositionnedSlot.getValidSlots(SlotHelper.PURE_DAISY_SLOTS, slots), param);
                break;
            case BREWERY:
                createBreweryRecipe(PositionnedSlot.getValidSlots(SlotHelper.BREWERY_SLOTS, slots));
                break;
            case PETAL_APOTHECARY:
                createPetalRecipe(PositionnedSlot.getValidSlots(SlotHelper.PETAL_APOTHECARY_SLOTS, slots), taggedSlots);
                break;
            case RUNIC_ALTAR:
                createRuneRecipe(PositionnedSlot.getValidSlots(SlotHelper.RUNIC_ALTAR_SLOTS, slots), taggedSlots, param);
                break;
            case TERRA_PLATE:
                createTerraPlateRecipe(PositionnedSlot.getValidSlots(SlotHelper.TERRA_PLATE_SLOTS, slots), taggedSlots, param);
                break;
        }
    }

    private static void createManaInfusionRecipe(List<Slot> slots, Object param)
    {
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item catalystItem = slots.get(1).getItem().getItem();
        ItemStack output = getValidOutput(slots);

        if(catalystItem instanceof BlockItem)
            BotaniaRecipesJSSerializer.get().createInfusionRecipe(input, ((BlockItem) catalystItem).getBlock(), output, (int) param);
        else
            BotaniaRecipesJSSerializer.get().createInfusionRecipe(input, Blocks.AIR, output, (int) param);
    }

    private static void createElvenTradeRecipe(List<Slot> slots, Object param)
    {
        List<Item> input = getValidList(slots, 0, 5);
        List<Item> output = getValidList(slots, 5, 10);

        if(!input.isEmpty() && !output.isEmpty())
        {
            BotaniaRecipesJSSerializer.get().createElvenTradeRecipe(input, output);
        }
    }

    private static void createPureDaisyRecipe(List<Slot> slots, Object param)
    {
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item output = getValidOutput(slots).getItem();

        if(input instanceof BlockItem && output instanceof BlockItem)
        {
            Block inputBlock = ((BlockItem) input).getBlock();
            Block outputBlock = ((BlockItem) output).getBlock();

            BotaniaRecipesJSSerializer.get().createPureDaisyRecipe(inputBlock, outputBlock, (int) param);
        }
    }

    private static void createBreweryRecipe(List<Slot> slots)
    {
        if(getValidOutput(slots) == null)
            return;

        Item brewItem = getValidOutput(slots).getItem();
        List<Item> ingredients = getValidIngredients(slots);

        if(brewItem instanceof IBrewItem)
        {
            Brew brew = ((IBrewItem) brewItem).getBrew(getValidOutput(slots));

            BotaniaRecipesJSSerializer.get().createBrewRecipe(ingredients, brew);
        }
    }

    private static void createPetalRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createPetalRecipe(input, output);
    }

    private static void createRuneRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, Object param)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createRuneRecipe(input, output, getMana(param));
    }

    private static void createTerraPlateRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, Object param)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createTerraPlateRecipe(input, output, getMana(param));
    }

    @Nullable
    private static ItemStack getValidOutput(List<Slot> slots)
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

    private static List<Item> getValidList(List<Slot> slots, int start, int end)
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

    private static List<Item> getValidIngredients(List<Slot> slots)
    {
        List<Item> list = new ArrayList<>();

        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i).hasItem())
                list.add(slots.get(i).getItem().getItem());
        }

        return list;
    }

    private static boolean isValid(ItemStack stack)
    {
        return stack != null && !stack.isEmpty();
    }

    private static Multimap<ResourceLocation, Boolean> getValidIngredients(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
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

    private static int getMana(Object param)
    {
        if(param instanceof Integer)
            return (int) param;

        return 100;
    }
}
