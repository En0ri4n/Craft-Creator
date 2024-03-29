package fr.eno.craftcreator.recipes.managers;

import fr.eno.craftcreator.base.RecipeCreator;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.recipes.base.BaseRecipesManager;
import fr.eno.craftcreator.recipes.base.ModRecipeSerializer;
import fr.eno.craftcreator.recipes.serializers.BotaniaRecipeSerializer;
import fr.eno.craftcreator.recipes.utils.RecipeEntry;
import fr.eno.craftcreator.recipes.utils.RecipeInfos;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewItem;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static fr.eno.craftcreator.base.ModRecipeCreators.*;

public class BotaniaRecipesManager extends BaseRecipesManager
{
    private static final BotaniaRecipesManager INSTANCE = new BotaniaRecipesManager();

    @Override
    public void createRecipe(RecipeCreator recipe, List<Slot> slots, RecipeInfos recipeInfos, ModRecipeSerializer.SerializerType serializerType)
    {
        BotaniaRecipeSerializer.get().setSerializerType(serializerType);

        List<SlotItemHandler> currentSlots = PositionnedSlot.getSlotsFor(recipe.getSlots(), slots);
        Map<Integer, ResourceLocation> taggedSlots = recipeInfos.getMap(RecipeInfos.Parameters.TAGGED_SLOTS);

        if(recipe.is(MANA_INFUSION))
            createManaInfusionRecipe(currentSlots, recipeInfos.getValue(RecipeInfos.Parameters.MANA).intValue());
        else if(recipe.is(ELVEN_TRADE))
            createElvenTradeRecipe(currentSlots, taggedSlots);
        else if(recipe.is(PURE_DAISY))
            createPureDaisyRecipe(currentSlots, recipeInfos.getValue(RecipeInfos.Parameters.TIME).intValue());
        else if(recipe.is(BREWERY))
            createBreweryRecipe(currentSlots);
        else if(recipe.is(PETAL_APOTHECARY))
            createPetalRecipe(currentSlots, taggedSlots);
        else if(recipe.is(RUNIC_ALTAR))
            createRuneRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.MANA).intValue());
        else if(recipe.is(TERRA_PLATE))
            createTerraPlateRecipe(currentSlots, taggedSlots, recipeInfos.getValue(RecipeInfos.Parameters.MANA).intValue());
    }

    private void createManaInfusionRecipe(List<SlotItemHandler> slots, int mana)
    {
        if(areSlotsEmpty(slots, SlotHelper.MANA_INFUSION_SLOTS_INPUT.size(), SlotHelper.MANA_INFUSION_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Input input = getSingleInput(Collections.emptyMap(), slots.get(0));
        RecipeEntry.BlockInput catalystItem = getBlockInput(slots.get(1));
        RecipeEntry.Output output = getValidOutput(slots, SlotHelper.MANA_INFUSION_SLOTS_OUTPUT.size());

        BotaniaRecipeSerializer.get().serializeInfusionRecipe(input, catalystItem, output, mana);
    }

    private void createElvenTradeRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(areSlotsEmpty(slots, SlotHelper.ELVEN_TRADE_SLOTS_INPUT.size(), SlotHelper.ELVEN_TRADE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.MultiInput input = getValidInputs(slots, taggedSlots, 0, 5);
        RecipeEntry.MultiOutput output = getValidOutputs(slots, 5, 10);

        BotaniaRecipeSerializer.get().serializeElvenTradeRecipe(input, output);
    }

    private void createPureDaisyRecipe(List<SlotItemHandler> slots, int time)
    {
        if(areSlotsEmpty(slots, SlotHelper.PURE_DAISY_SLOTS_INPUT.size(), SlotHelper.PURE_DAISY_SLOTS_OUTPUT.size())) return;

        RecipeEntry.BlockInput input = getBlockInput(slots.get(0));
        RecipeEntry.BlockOutput output = getBlockOutput(slots.get(1));

        BotaniaRecipeSerializer.get().serializePureDaisyRecipe(input, output, time);
    }

    private void createBreweryRecipe(List<SlotItemHandler> slots)
    {
        if(areSlotsEmpty(slots, SlotHelper.BREWERY_SLOTS_INPUT.size(), SlotHelper.BREWERY_SLOTS_OUTPUT.size())) return;

        RecipeEntry.MultiInput ingredients = getValidIngredients(slots);

        RecipeEntry.Output brewOutput = getValidOutput(slots, SlotHelper.BREWERY_SLOTS_OUTPUT.size());
        Brew brew = ((IBrewItem) brewOutput.getItem()).getBrew(brewOutput.getItem().getDefaultInstance());

        BotaniaRecipeSerializer.get().serializeBrewRecipe(ingredients, brew);
    }

    private void createPetalRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots)
    {
        if(areSlotsEmpty(slots, SlotHelper.PETAL_APOTHECARY_SLOTS_INPUT.size(), SlotHelper.PETAL_APOTHECARY_SLOTS_OUTPUT.size())) return;

        RecipeEntry.MultiInput input = getValidIngredients(slots, taggedSlots);
        RecipeEntry.Output output = getValidOutput(slots, SlotHelper.PETAL_APOTHECARY_SLOTS_OUTPUT.size());

        BotaniaRecipeSerializer.get().serializePetalRecipe(input, output);
    }

    private void createRuneRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(areSlotsEmpty(slots, SlotHelper.RUNIC_ALTAR_SLOTS_INPUT.size(), SlotHelper.RUNIC_ALTAR_SLOTS_OUTPUT.size())) return;

        RecipeEntry.MultiInput input = getValidIngredients(slots, taggedSlots);
        RecipeEntry.Output output = getValidOutput(slots, SlotHelper.RUNIC_ALTAR_SLOTS_OUTPUT.size());

        BotaniaRecipeSerializer.get().serializeRuneRecipe(input, output, mana);
    }

    private void createTerraPlateRecipe(List<SlotItemHandler> slots, Map<Integer, ResourceLocation> taggedSlots, int mana)
    {
        if(areSlotsEmpty(slots, SlotHelper.TERRA_PLATE_SLOTS_INPUT.size(), SlotHelper.TERRA_PLATE_SLOTS_OUTPUT.size())) return;

        RecipeEntry.Output output = getValidOutput(slots, SlotHelper.TERRA_PLATE_SLOTS_OUTPUT.size());
        RecipeEntry.MultiInput input = getValidIngredients(slots, taggedSlots);

        BotaniaRecipeSerializer.get().serializeTerraPlateRecipe(input, output, mana);
    }

    public static BotaniaRecipesManager get()
    {
        return INSTANCE;
    }
}
