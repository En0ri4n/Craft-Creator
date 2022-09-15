package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class EntryHelper
{
    private static final List<ResourceLocation> items = new ArrayList<>();
    private static final List<String> mods = new ArrayList<>();
    private static final List<ResourceLocation> recipeTypes = new ArrayList<>();
    private static final List<ResourceLocation> recipeIds = new ArrayList<>();

    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryList(List<String> list)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(rl -> entries.add((T) new SimpleListWidget.StringEntry(rl)));

        return entries;
    }

    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryListWith(List<ResourceLocation> list)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(rl -> entries.add((T) new SimpleListWidget.ResourceLocationEntry(rl)));

        return entries;
    }

    public static <T extends Recipe<C>, C extends Container> void init(Level world)
    {
        if(items.isEmpty())
            ForgeRegistries.ITEMS.getValues().forEach(item -> items.add(item.getRegistryName()));

        if(recipeTypes.isEmpty())
            Registry.RECIPE_TYPE.forEach(irecipetype -> recipeTypes.add(Registry.RECIPE_TYPE.getKey(irecipetype)));

        if(mods.isEmpty())
            ModList.get().getMods().forEach(mod -> mods.add(mod.getModId()));

        if(recipeIds.isEmpty())
            Registry.RECIPE_TYPE.stream().collect(Collectors.toList()).forEach(recipeType ->
        {
            RecipeType<T> recipeType1 = (RecipeType<T>) recipeType;
            world.getRecipeManager().getAllRecipesFor(recipeType1).forEach(recipe ->
            {
                if(!recipe.getId().toString().contains("kjs")) recipeIds.add(recipe.getId());
            });
        });
    }

    public static List<ResourceLocation> getItems()
    {
        return items;
    }

    public static List<String> getMods()
    {
        return mods;
    }

    public static List<ResourceLocation> getRecipeTypes()
    {
        return recipeTypes;
    }

    public static List<ResourceLocation> getRecipeIds()
    {
        return recipeIds;
    }
}
