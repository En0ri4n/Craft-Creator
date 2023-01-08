package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.Multimap;
import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreator;
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

import java.util.List;
import java.util.Map;

public class BotaniaRecipesManager extends BaseRecipesManager
{
    private static final BotaniaRecipesManager INSTANCE = new BotaniaRecipesManager();

    @Override
    public void createRecipe(ModRecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case MANA_INFUSION ->
                    createManaInfusionRecipe(PositionnedSlot.getSlotsFor(SlotHelper.MANA_INFUSION_SLOTS, slots), recipeInfos.getValue("mana").intValue());
            case ELVEN_TRADE ->
                    createElvenTradeRecipe(PositionnedSlot.getSlotsFor(SlotHelper.ELVEN_TRADE_SLOTS, slots));
            case PURE_DAISY ->
                    createPureDaisyRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, slots), recipeInfos.getValue("time").intValue());
            case BREWERY -> createBreweryRecipe(PositionnedSlot.getSlotsFor(SlotHelper.BREWERY_SLOTS, slots));
            case PETAL_APOTHECARY ->
                    createPetalRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PETAL_APOTHECARY_SLOTS, slots), recipeInfos.getMap("tagged_slots"));
            case RUNIC_ALTAR ->
                    createRuneRecipe(PositionnedSlot.getSlotsFor(SlotHelper.RUNIC_ALTAR_SLOTS, slots), recipeInfos.getMap("tagged_slots"), recipeInfos.getValue("mana").intValue());
            case TERRA_PLATE ->
                    createTerraPlateRecipe(PositionnedSlot.getSlotsFor(SlotHelper.TERRA_PLATE_SLOTS, slots), recipeInfos.getMap("tagged_slots"), recipeInfos.getValue("mana").intValue());
        }
    }

    private void createManaInfusionRecipe(List<Slot> slots, int mana)
    {
        if(isSlotsEmpty(slots, SlotHelper.MANA_INFUSION_SLOTS_INPUT.size(), SlotHelper.MANA_INFUSION_SLOTS_OUTPUT.size()))
            return;

        Item input = slots.get(0).getItem().getItem();
        Item catalystItem = slots.get(1).getItem().getItem();
        ItemStack output = getValidOutput(slots, SlotHelper.MANA_INFUSION_SLOTS_OUTPUT.size());

        if(catalystItem instanceof BlockItem)
            BotaniaRecipesSerializer.get().createInfusionRecipe(input, ((BlockItem) catalystItem).getBlock(), output, mana);
        else
            BotaniaRecipesSerializer.get().createInfusionRecipe(input, Blocks.AIR, output, mana);
    }

    private void createElvenTradeRecipe(List<Slot> slots)
    {
        List<Item> input = getValidList(slots, 0, 5);
        List<Item> output = getValidList(slots, 5, 10);

        if(!input.isEmpty() && !output.isEmpty())
        {
            BotaniaRecipesSerializer.get().createElvenTradeRecipe(input, output);
        }
    }

    private void createPureDaisyRecipe(List<Slot> slots, int time)
    {
        if(isSlotsEmpty(slots, SlotHelper.PURE_DAISY_SLOTS_INPUT.size(), SlotHelper.PURE_DAISY_SLOTS_OUTPUT.size()))
            return;

        Item input = slots.get(0).getItem().getItem();
        Item output = getValidOutput(slots, SlotHelper.PURE_DAISY_SLOTS_OUTPUT.size()).getItem();

        if(input instanceof BlockItem && output instanceof BlockItem)
        {
            Block inputBlock = ((BlockItem) input).getBlock();
            Block outputBlock = ((BlockItem) output).getBlock();

            BotaniaRecipesSerializer.get().createPureDaisyRecipe(inputBlock, outputBlock, time);
        }
    }

    private void createBreweryRecipe(List<Slot> slots)
    {
        if(isSlotsEmpty(slots, SlotHelper.BREWERY_SLOTS_INPUT.size(), SlotHelper.BREWERY_SLOTS_OUTPUT.size()))
            return;

        ItemStack brewItemstack = getValidOutput(slots, SlotHelper.BREWERY_SLOTS_OUTPUT.size());
        List<Item> ingredients = getValidIngredients(slots);

        if(brewItemstack.getItem() instanceof IBrewItem)
        {
            Brew brew = ((IBrewItem) brewItemstack.getItem()).getBrew(brewItemstack);

            BotaniaRecipesSerializer.get().createBrewRecipe(ingredients, brew);
        }
    }

    private void createPetalRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(isSlotsEmpty(slots, SlotHelper.PETAL_APOTHECARY_SLOTS_INPUT.size(), SlotHelper.PETAL_APOTHECARY_SLOTS_OUTPUT.size()))
            return;

        ItemStack output = getValidOutput(slots, SlotHelper.PETAL_APOTHECARY_SLOTS_OUTPUT.size());
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createPetalRecipe(input, output);
    }

    private void createRuneRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(isSlotsEmpty(slots, SlotHelper.RUNIC_ALTAR_SLOTS_INPUT.size(), SlotHelper.RUNIC_ALTAR_SLOTS_OUTPUT.size()))
            return;

        ItemStack output = getValidOutput(slots, SlotHelper.RUNIC_ALTAR_SLOTS_OUTPUT.size());
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createRuneRecipe(input, output, mana);
    }

    private void createTerraPlateRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(isSlotsEmpty(slots, SlotHelper.TERRA_PLATE_SLOTS_INPUT.size(), SlotHelper.TERRA_PLATE_SLOTS_OUTPUT.size()))
            return;

        ItemStack output = getValidOutput(slots, SlotHelper.TERRA_PLATE_SLOTS_OUTPUT.size());
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createTerraPlateRecipe(input, output, mana);
    }

    public static BotaniaRecipesManager get()
    {
        return INSTANCE;
    }
}
