package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.client.utils.ClientUtils;
import fr.eno.craftcreator.recipes.utils.EntryType;
import fr.eno.craftcreator.client.screen.widgets.SimpleListWidget;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
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

    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryListWith(List<ResourceLocation> list, EntryType type)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(rl -> entries.add((T) new SimpleListWidget.ResourceLocationEntry(rl, type)));

        return entries;
    }

    public static List<ResourceLocation> getItems()
    {
        return ForgeRegistries.ITEMS.getValues().stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList());
    }

    public static List<ResourceLocation> getFluids()
    {
        return ForgeRegistries.FLUIDS.getValues().stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList());
    }

    public static List<String> getMods()
    {
        return ModList.get().getMods().stream().map(ModInfo::getModId).collect(Collectors.toList());
    }

    public static List<ResourceLocation> getRecipeTypes()
    {
        return Registry.RECIPE_TYPE.stream().map(Registry.RECIPE_TYPE::getKey).collect(Collectors.toList());
    }

    public static <C extends net.minecraft.inventory.IInventory,
            T extends net.minecraft.item.crafting.IRecipe<C>> List<ResourceLocation> getRecipeIds()
    {
        return Registry.RECIPE_TYPE.stream()
                .map(rt ->
                        {
                            IRecipeType<T> recipeType = (IRecipeType<T>) rt;
                            return ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).stream()
                                    .filter(recipe -> !recipe.getId().toString().contains("kjs"))
                                    .collect(Collectors.toList());
                        })
                .flatMap(Collection::stream)
                .map(IRecipe::getId)
                .collect(Collectors.toList());
    }

    public static List<ResourceLocation> getTags()
    {
        return new ArrayList<>(ItemTags.getAllTags().getAvailableTags());
    }
}
