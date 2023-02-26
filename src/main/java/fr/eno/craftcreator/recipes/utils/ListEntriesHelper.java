package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.base.SupportedMods;
import fr.eno.craftcreator.recipes.kubejs.KubeJSHelper;
import fr.eno.craftcreator.screen.widgets.SimpleListWidget;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ListEntriesHelper
{
    public static <C extends Container, R extends Recipe<C>, T extends SimpleListWidget.Entry> List<T> getAddedRecipesEntryList(SupportedMods mod, ResourceLocation recipeTypeLoc)
    {
        RecipeType<R> recipeType = CommonUtils.getRecipeTypeByName(recipeTypeLoc);

        List<T> entries = new ArrayList<>();

        ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).stream().filter(recipe -> recipe.getId().getNamespace().equals(References.MOD_ID)).forEach(recipe -> entries.add((T) new SimpleListWidget.RecipeEntry(recipe)));

        if(SupportedMods.isKubeJSLoaded()) KubeJSHelper.getAddedRecipesFor(mod, recipeType).forEach(recipe -> entries.add((T) new SimpleListWidget.RecipeEntry(recipe)));

        return entries;
    }

    public static <T extends SimpleListWidget.ModifiedRecipeEntry> List<T> getModifiedRecipesEntryList(SupportedMods mod)
    {
        List<T> entries = new ArrayList<>();

        if(!SupportedMods.isKubeJSLoaded()) return entries;

        KubeJSHelper.getModifiedRecipes(mod).forEach(modifiedRecipe -> entries.add((T) new SimpleListWidget.ModifiedRecipeEntry(modifiedRecipe)));

        return entries;
    }

    public static <C extends Container, T extends Recipe<C>, E extends SimpleListWidget.Entry> List<E> getRecipes(ResourceLocation recipeTypeLoc)
    {
        RecipeType<T> recipeType = CommonUtils.getRecipeTypeByName(recipeTypeLoc);

        List<E> entries = new ArrayList<>();

        ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).forEach(recipe ->
        {
            if(!recipe.getId().toString().contains("kjs") && !recipe.getId().getNamespace().equals(References.MOD_ID)) entries.add((E) new SimpleListWidget.RecipeEntry(recipe));
        });

        return entries.stream().sorted(Comparator.comparing(object -> ((SimpleListWidget.RecipeEntry) object).getRecipe().getId().toString())).collect(Collectors.toList());
    }

    public static <C extends Container, T extends Recipe<C>, E extends SimpleListWidget.Entry> List<E> getFilteredRecipes(ResourceLocation recipeTypeLoc, String filter)
    {
        RecipeType<T> recipeType = CommonUtils.getRecipeTypeByName(recipeTypeLoc);

        List<E> entries = new ArrayList<>();

        ClientUtils.getClientLevel().getRecipeManager().getAllRecipesFor(recipeType).forEach(recipe ->
        {
            if(!recipe.getId().toString().contains("kjs") && recipe.getId().toString().contains(filter)) entries.add((E) new SimpleListWidget.RecipeEntry(recipe));
        });

        return entries.stream().sorted(Comparator.comparing(object -> ((SimpleListWidget.RecipeEntry) object).getRecipe().getId().toString())).collect(Collectors.toList());
    }

    public enum RecipeList
    {
        ADDED_RECIPES,
        MODIFIED_RECIPES
    }
}
