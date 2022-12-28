package fr.eno.craftcreator.kubejs.managers;

import cofh.thermal.lib.common.ThermalRecipeManagers;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.utils.ModRecipeCreatorScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Map;

public class RecipeManagerDispatcher
{
    public static void createRecipe(SupportedMods mod, ModRecipeCreatorScreens recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        switch(mod)
        {
            case BOTANIA -> BotaniaRecipesManager.get().createRecipe(recipe, slots, taggedSlots, param);
            case THERMAL -> ThermalRecipesManager.get().createRecipe(recipe, slots, taggedSlots, param);
        }
    }
}
