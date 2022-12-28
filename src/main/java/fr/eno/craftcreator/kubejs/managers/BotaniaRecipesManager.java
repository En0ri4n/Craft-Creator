package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesJSSerializer;
import fr.eno.craftcreator.screen.utils.ModRecipeCreatorScreens;
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

public class BotaniaRecipesManager extends BaseRecipesManager
{
    private static final BotaniaRecipesManager INSTANCE = new BotaniaRecipesManager();

    private final int MANA_INDEX = 0;

    @Override
    public void createRecipe(ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        switch(recipe)
        {
            case MANA_INFUSION ->
                    createManaInfusionRecipe(PositionnedSlot.getSlotsFor(SlotHelper.MANA_INFUSION_SLOTS, slots), param);
            case ELVEN_TRADE ->
                    createElvenTradeRecipe(PositionnedSlot.getSlotsFor(SlotHelper.ELVEN_TRADE_SLOTS, slots), param);
            case PURE_DAISY ->
                    createPureDaisyRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, slots), param);
            case BREWERY -> createBreweryRecipe(PositionnedSlot.getSlotsFor(SlotHelper.BREWERY_SLOTS, slots));
            case PETAL_APOTHECARY ->
                    createPetalRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PETAL_APOTHECARY_SLOTS, slots), taggedSlots);
            case RUNIC_ALTAR ->
                    createRuneRecipe(PositionnedSlot.getSlotsFor(SlotHelper.RUNIC_ALTAR_SLOTS, slots), taggedSlots, param);
            case TERRA_PLATE ->
                    createTerraPlateRecipe(PositionnedSlot.getSlotsFor(SlotHelper.TERRA_PLATE_SLOTS, slots), taggedSlots, param);
        }
    }

    private void createManaInfusionRecipe(List<Slot> slots, List<Integer> param)
    {
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item catalystItem = slots.get(1).getItem().getItem();
        ItemStack output = getValidOutput(slots);

        if(catalystItem instanceof BlockItem)
            BotaniaRecipesJSSerializer.get().createInfusionRecipe(input, ((BlockItem) catalystItem).getBlock(), output, param.get(MANA_INDEX));
        else
            BotaniaRecipesJSSerializer.get().createInfusionRecipe(input, Blocks.AIR, output, param.get(MANA_INDEX));
    }

    private void createElvenTradeRecipe(List<Slot> slots, List<Integer> param)
    {
        List<Item> input = getValidList(slots, 0, 5);
        List<Item> output = getValidList(slots, 5, 10);

        if(!input.isEmpty() && !output.isEmpty())
        {
            BotaniaRecipesJSSerializer.get().createElvenTradeRecipe(input, output);
        }
    }

    private void createPureDaisyRecipe(List<Slot> slots, List<Integer> param)
    {
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item output = getValidOutput(slots).getItem();

        if(input instanceof BlockItem && output instanceof BlockItem)
        {
            Block inputBlock = ((BlockItem) input).getBlock();
            Block outputBlock = ((BlockItem) output).getBlock();

            BotaniaRecipesJSSerializer.get().createPureDaisyRecipe(inputBlock, outputBlock, param.get(MANA_INDEX));
        }
    }

    private void createBreweryRecipe(List<Slot> slots)
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

    private void createPetalRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createPetalRecipe(input, output);
    }

    private void createRuneRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createRuneRecipe(input, output, param.get(MANA_INDEX));
    }

    private void createTerraPlateRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesJSSerializer.get().createTerraPlateRecipe(input, output, param.get(MANA_INDEX));
    }

    public static BotaniaRecipesManager get()
    {
        return INSTANCE;
    }
}
