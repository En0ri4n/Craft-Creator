package fr.eno.craftcreator.kubejs.utils;

import fr.eno.craftcreator.kubejs.jsserializers.BotaniaRecipesJSSerializer;
import fr.eno.craftcreator.kubejs.jsserializers.MinecraftRecipeSerializer;
import fr.eno.craftcreator.kubejs.jsserializers.ModRecipesJSSerializer;
import fr.eno.craftcreator.utils.PairValue;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemVial;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class ModDispatcher
{
    @Nullable
    public static PairValue<String, Integer> getParameters(IRecipe<?> recipe)
    {
        String modId = recipe.getId().getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getParam(recipe);
        }

        return null;
    }

    public static ModRecipesJSSerializer getSeralizer(String modId)
    {
        switch(modId)
        {
            case SupportedMods.BOTANIA:
                return BotaniaRecipesJSSerializer.get();
            case SupportedMods.MINECRAFT:
            default:
                return MinecraftRecipeSerializer.get();
        }
    }

    public static Map<String, ResourceLocation> getOutput(IRecipe<?> recipe)
    {
        String modId = recipe.getId().getNamespace();

        if(SupportedMods.isModLoaded(modId))
        {
            return getSeralizer(modId).getOutput(recipe);
        }

        return null;
    }

    public static ItemStack getOneOutput(IRecipe<?> recipe)
    {
        ItemStack stack = ItemStack.EMPTY;
        Map<String, ResourceLocation> locations = ModDispatcher.getOutput(recipe);

        Map.Entry<String, ResourceLocation> entry = locations.entrySet().stream().findFirst().orElse(null);

        if(SupportedMods.isModLoaded(entry.getValue().getNamespace()))
        {
            return getSeralizer(entry.getValue().getNamespace()).getOneOutput(entry);
        }

        return stack;
    }
}
