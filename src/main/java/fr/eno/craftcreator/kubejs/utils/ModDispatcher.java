package fr.eno.craftcreator.kubejs.utils;

import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.jsserializers.ThermalRecipesJSSerializer;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModDispatcher
{
    public static ModRecipesJSSerializer getSeralizer(String modId)
    {
        switch(SupportedMods.getMod(modId))
        {
            case BOTANIA:
                return BotaniaRecipesJSSerializer.get();
            case THERMAL:
                return ThermalRecipesJSSerializer.get();
            case MINECRAFT:
            default:
                return MinecraftRecipeSerializer.get();
        }
    }

    @Nullable
    public static PairValue<String, Integer> getParameters(Recipe<?> recipe)
    {
        String modId = recipe.getId().getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getParam(recipe);
        }

        return PairValue.create("Unknown", 0);
    }

    public static Map<String, ResourceLocation> getOutput(Recipe<?> recipe)
    {
        String modId = recipe.getId().getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getOutput(recipe);
        }

        return new HashMap<>();
    }

    public static ItemStack getOneOutput(Recipe<?> recipe)
    {
        ItemStack stack = ItemStack.EMPTY;
        Map<String, ResourceLocation> locations = getOutput(recipe);

        Map.Entry<String, ResourceLocation> entry = locations.entrySet().stream().findFirst().orElse(null);

        if(SupportedMods.isModLoaded(entry.getValue().getNamespace()))
        {
            return getSeralizer(entry.getValue().getNamespace()).getOneOutput(entry);
        }

        return stack;
    }
}
