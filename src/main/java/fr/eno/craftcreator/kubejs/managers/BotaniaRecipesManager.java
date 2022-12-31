package fr.eno.craftcreator.kubejs.managers;

import com.google.common.collect.Multimap;
import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
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

import java.util.List;
import java.util.Map;

public class BotaniaRecipesManager extends BaseRecipesManager
{
    private static final BotaniaRecipesManager INSTANCE = new BotaniaRecipesManager();

    @Override
    public void createRecipe(ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case MANA_INFUSION ->
                    createManaInfusionRecipe(PositionnedSlot.getSlotsFor(SlotHelper.MANA_INFUSION_SLOTS, slots), recipeInfos.getInteger("mana"));
            case ELVEN_TRADE ->
                    createElvenTradeRecipe(PositionnedSlot.getSlotsFor(SlotHelper.ELVEN_TRADE_SLOTS, slots));
            case PURE_DAISY ->
                    createPureDaisyRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PURE_DAISY_SLOTS, slots), recipeInfos.getInteger("time"));
            case BREWERY -> createBreweryRecipe(PositionnedSlot.getSlotsFor(SlotHelper.BREWERY_SLOTS, slots));
            case PETAL_APOTHECARY ->
                    createPetalRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PETAL_APOTHECARY_SLOTS, slots), taggedSlots);
            case RUNIC_ALTAR ->
                    createRuneRecipe(PositionnedSlot.getSlotsFor(SlotHelper.RUNIC_ALTAR_SLOTS, slots), taggedSlots, recipeInfos.getInteger("mana"));
            case TERRA_PLATE ->
                    createTerraPlateRecipe(PositionnedSlot.getSlotsFor(SlotHelper.TERRA_PLATE_SLOTS, slots), taggedSlots, recipeInfos.getInteger("mana"));
        }
    }

    private void createManaInfusionRecipe(List<Slot> slots, int mana)
    {
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item catalystItem = slots.get(1).getItem().getItem();
        ItemStack output = getValidOutput(slots);

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
        if(getValidOutput(slots) == null)
            return;

        Item input = slots.get(0).getItem().getItem();
        Item output = getValidOutput(slots).getItem();

        if(input instanceof BlockItem && output instanceof BlockItem)
        {
            Block inputBlock = ((BlockItem) input).getBlock();
            Block outputBlock = ((BlockItem) output).getBlock();

            BotaniaRecipesSerializer.get().createPureDaisyRecipe(inputBlock, outputBlock, time);
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

            BotaniaRecipesSerializer.get().createBrewRecipe(ingredients, brew);
        }
    }

    private void createPetalRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createPetalRecipe(input, output);
    }

    private void createRuneRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createRuneRecipe(input, output, mana);
    }

    private void createTerraPlateRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack output = getValidOutput(slots);
        Multimap<ResourceLocation, Boolean> input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipesSerializer.get().createTerraPlateRecipe(input, output, mana);
    }

    public static BotaniaRecipesManager get()
    {
        return INSTANCE;
    }
}
