package fr.eno.craftcreator.kubejs.managers;

import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.screen.ModRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Map;

public class RecipeManagerDispatcher
{
    public static void createRecipe(SupportedMods mod, ModRecipes recipe, List<Slot> slots, Map<Integer, ResourceLocation> taggedSlots, List<Integer> param)
    {
        switch(mod)
        {
            case BOTANIA -> BotaniaRecipeManagers.createRecipe(recipe, slots, taggedSlots, param);
        }
    }
}
