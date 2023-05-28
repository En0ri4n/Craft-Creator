package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
/*
 * This class is ONLY for the client side, it's used to help the screen to get the entries
 */
public class EntryHelper
{
    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryList(List<String> list)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(rl -> entries.add((T) new SimpleListWidget.StringEntry(rl)));

        return entries;
    }

    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryListWith(List<ResourceLocation> list, SimpleListWidget.ResourceLocationEntry.Type type)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(rl -> entries.add((T) new SimpleListWidget.ResourceLocationEntry(rl, type)));

        return entries;
    }

    public static List<ResourceLocation> getItems()
    {
        return ForgeRegistries.ITEMS.getValues().stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList());
    }

    public static List<String> getMods()
    {
        return ModList.get().getMods().stream().map(IModInfo::getModId).collect(Collectors.toList());
    }

    public static List<ResourceLocation> getRecipeTypes()
    {
        return Registry.RECIPE_TYPE.stream().map(Registry.RECIPE_TYPE::getKey).collect(Collectors.toList());
    }

    public static <C extends Container,
            T extends Recipe<C>> List<ResourceLocation> getRecipeIds()
    {
        return Registry.RECIPE_TYPE.stream()
                .map(rt ->
                        {
                            RecipeType<T> recipeType = (RecipeType<T>) rt;
                            return ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).stream()
                                    .filter(recipe -> !recipe.getId().toString().contains("kjs"))
                                    .collect(Collectors.toList());
                        })
                .flatMap(Collection::stream)
                .map(Recipe::getId)
                .collect(Collectors.toList());
    }

    public static List<ResourceLocation> getTags()
    {
        return ForgeRegistries.ITEMS.tags().stream().map(itag -> itag.getKey().location()).collect(Collectors.toList());
    }
}
