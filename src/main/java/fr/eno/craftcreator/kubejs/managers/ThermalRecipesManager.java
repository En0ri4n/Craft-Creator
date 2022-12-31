package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.jsserializers.ThermalRecipesSerializer;
import fr.eno.craftcreator.kubejs.utils.RecipeInfos;
import fr.eno.craftcreator.screen.utils.ModRecipeCreatorScreens;
import fr.eno.craftcreator.utils.PairValue;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThermalRecipesManager extends BaseRecipesManager
{
    private static final ThermalRecipesManager INSTANCE = new ThermalRecipesManager();

    public void createRecipe(ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        switch(recipe)
        {
            case TREE_EXTRACTOR -> createTreeExtractorRecipe(PositionnedSlot.getSlotsFor(SlotHelper.TREE_EXTRACTOR_SLOTS, slots), recipeInfos.getInteger("resin_amount"));
            case PULVERIZER -> createPulverizerRecipe(PositionnedSlot.getSlotsFor(SlotHelper.PULVERIZER_SLOTS, slots), taggedSlots, recipeInfos);
            case SAWMILL -> createSawmillRecipe(PositionnedSlot.getSlotsFor(SlotHelper.SAWMILL_SLOTS, slots), taggedSlots, recipeInfos);
        }
    }

    private void createTreeExtractorRecipe(List<Slot> slots, int resin_amount)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots).getItem()).getFluid();

        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || resin_amount <= 0)
            return;

        ThermalRecipesSerializer.get().createTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, resin_amount);
    }

    private void createPulverizerRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(!hasItems(slots))
            return;

        PairValue<ResourceLocation, PairValue<Map<ItemStack, Double>, Double>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().createPulverizerRecipe(values.getFirstValue(), values.getSecondValue().getFirstValue(), values.getSecondValue().getSecondValue());
    }

    private void createSawmillRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        if(!hasItems(slots))
            return;

        PairValue<ResourceLocation, PairValue<Map<ItemStack, Double>, Double>> values = processRecipe(slots, taggedSlots, recipeInfos);

        ThermalRecipesSerializer.get().createSawmillRecipe(values.getFirstValue(), values.getSecondValue().getFirstValue(), values.getSecondValue().getSecondValue());
    }

    private static PairValue<ResourceLocation, PairValue<Map<ItemStack, Double>, Double>> processRecipe(List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, RecipeInfos recipeInfos)
    {
        ResourceLocation input = slots.get(0).getItem().getItem().getRegistryName();
        Map<ItemStack, Double> outputs = new HashMap<>();
        double energy = Math.round(recipeInfos.getDouble("energy"));
        for(int i = 0; i < slots.size() - 1; i++)
        {
            if(slots.get(i + 1).hasItem())
                outputs.put(slots.get(i + 1).getItem(), recipeInfos.getDouble("chance_" + i));
        }

        if(!taggedSlots.isEmpty())
        {
            for(Map.Entry<Integer, ResourceLocation> taggedSlot : taggedSlots.entrySet())
            {
                if(taggedSlot.getKey() == slots.get(0).getSlotIndex())
                {
                    input = taggedSlot.getValue();
                    break;
                }
            }
        }

        return PairValue.create(input, PairValue.create(outputs, energy));
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }
}
