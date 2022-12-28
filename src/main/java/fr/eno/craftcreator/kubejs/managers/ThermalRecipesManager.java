package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.jsserializers.ThermalRecipesJSSerializer;
import fr.eno.craftcreator.screen.utils.ModRecipeCreatorScreens;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Map;

public class ThermalRecipesManager extends BaseRecipesManager
{
    private static final ThermalRecipesManager INSTANCE = new ThermalRecipesManager();

    private static final int RESIN_AMOUNT_INDEX = 0;

    public void createRecipe(ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        switch(recipe)
        {
            case TREE_EXTRACTOR -> createTreeExtractorRecipe(PositionnedSlot.getSlotsFor(SlotHelper.TREE_EXTRACTOR_SLOTS, slots), param);
        }
    }

    private void createTreeExtractorRecipe(List<Slot> slots, List<Integer> param)
    {
        if(getValidOutput(slots) == null)
            return;

        ItemStack trunk = slots.get(0).getItem();
        ItemStack leaves = slots.get(1).getItem();
        Fluid fluid = ((BucketItem) getValidOutput(slots).getItem()).getFluid();
        int amount = param.get(RESIN_AMOUNT_INDEX);

        if(trunk.isEmpty() || leaves.isEmpty() || fluid == Fluids.EMPTY || amount <= 0)
            return;

        ThermalRecipesJSSerializer.get().createTreeExtractorRecipe(Block.byItem(trunk.getItem()), Block.byItem(leaves.getItem()), fluid, amount);
    }

    public static ThermalRecipesManager get()
    {
        return INSTANCE;
    }
}
