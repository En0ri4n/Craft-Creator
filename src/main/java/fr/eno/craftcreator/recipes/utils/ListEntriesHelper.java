package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListEntriesHelper
{
    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryList(SupportedMods[] mods)
    {
        List<T> entries = new ArrayList<>();

        SupportedMods.getSupportedLoadedMods().forEach(mod -> entries.add((T) new SimpleListWidget.StringEntry(mod.getModId())));

        return entries;
    }

    public static <C extends IInventory, R extends IRecipe<C>, T extends SimpleListWidget.Entry> List<T> getAddedRecipesEntryList(SupportedMods mod, ResourceLocation recipeTypeLoc)
    {
        IRecipeType<R> recipeType = CommonUtils.getRecipeTypeByName(recipeTypeLoc);

        List<T> entries = new ArrayList<>();

        if(!SupportedMods.isKubeJSLoaded())
        {
            ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).stream().filter(recipe -> recipe.getId().getNamespace().equals(References.MOD_ID)).forEach(recipe -> entries.add((T) new SimpleListWidget.RecipeEntry(recipe)));
        }
        else
        {
            KubeJSHelper.getAddedRecipesFor(mod, recipeType).forEach(recipe -> entries.add((T) new SimpleListWidget.RecipeEntry(recipe)));
        }

        return entries;
    }

    public static <T extends SimpleListWidget.ModifiedRecipeEntry> List<T> getModifiedRecipesEntryList(SupportedMods mod)
    {
        List<T> entries = new ArrayList<>();

        if(!SupportedMods.isKubeJSLoaded()) return entries;

        KubeJSHelper.getModifiedRecipes(mod).forEach(modifiedRecipe -> entries.add((T) new SimpleListWidget.ModifiedRecipeEntry(modifiedRecipe)));

        return entries;
    }

    public static <T extends SimpleListWidget.Entry> List<T> getRecipeTypes(String modId)
    {
        List<T> entries = new ArrayList<>();

        Registry.RECIPE_TYPE.stream().filter(type -> CommonUtils.getRecipeTypeName(type).getNamespace().equals(modId)).forEach(type -> entries.add((T) new SimpleListWidget.StringEntry(CommonUtils.getRecipeTypeName(type).toString())));

        return entries;
    }

    public static <C extends IInventory, T extends IRecipe<C>, E extends SimpleListWidget.Entry> List<E> getRecipes(ResourceLocation recipeTypeLoc)
    {
        IRecipeType<T> recipeType = CommonUtils.getRecipeTypeByName(recipeTypeLoc);

        List<E> entries = new ArrayList<>();

        ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).forEach(recipe ->
        {
            if(!recipe.getId().toString().contains("kjs")) entries.add((E) new SimpleListWidget.RecipeEntry(recipe));
        });

        return entries.stream().sorted(Comparator.comparing(object -> ((SimpleListWidget.RecipeEntry) object).getRecipe().getId().toString())).collect(Collectors.toList());
    }
}
