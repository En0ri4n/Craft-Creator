package fr.eno.craftcreator.kubejs.utils;

import fr.eno.craftcreator.screen.widgets.*;
import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.item.crafting.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DeserializerHelper
{
    public static <T extends SimpleListWidget.Entry> List<T> getStringEntryList(List<String> list)
    {
        List<T> entries = new ArrayList<>();

        list.forEach(s -> entries.add((T) new SimpleListWidget.StringEntry(s)));

        return entries;
    }



    public static <T extends SimpleListWidget.Entry> List<T> getAddedRecipesEntryList(String modId, ResourceLocation recipeTypeLoc)
    {
        IRecipeType<? extends IRecipe<?>> recipeType = RecipeFileUtils.byName(recipeTypeLoc);

        List<T> entries = new ArrayList<>();

        RecipeFileUtils.getAddedRecipesFor(modId, recipeType).forEach(recipe ->
                entries.add((T) new SimpleListWidget.RecipeEntry(recipe)));

        return entries;
    }

    public static <T extends SimpleListWidget.Entry> List<T> getModifiedRecipesEntryList(String modId, ResourceLocation recipeTypeLoc)
    {
        IRecipeType<? extends IRecipe<?>> recipeType = RecipeFileUtils.byName(recipeTypeLoc);

        List<T> entries = new ArrayList<>();

        RecipeFileUtils.getModifiedRecipesFor(modId, recipeType).forEach((pairValue, display) ->
                entries.add((T) new SimpleListWidget.StringEntry(pairValue.getSecondValue()).setTooltips(display)));

        return entries;
    }

    public static <T extends SimpleListWidget.Entry> List<T> getRecipeTypes(String modId)
    {
        List<T> entries = new ArrayList<>();

        Registry.RECIPE_TYPE.stream().filter(type -> RecipeFileUtils.getName(type).getNamespace().equals(modId))
                .forEach(type -> entries.add((T) new SimpleListWidget.StringEntry(RecipeFileUtils.getName(type).toString())));

        return entries;
    }

    public static <C extends IInventory, T extends IRecipe<C>, E extends SimpleListWidget.Entry> List<E> getRecipes(ResourceLocation recipeTypeLoc)
    {
        IRecipeType<T> recipeType = RecipeFileUtils.byName(recipeTypeLoc);

        List<E> entries = new ArrayList<>();

        Minecraft.getInstance().world.getRecipeManager().getRecipesForType(recipeType)
                .forEach(recipe ->
                {
                    if(!recipe.getId().toString().contains("kjs"))
                        entries.add((E) new SimpleListWidget.RecipeEntry(recipe));
                });

        return entries.stream().sorted((object1, object2) -> ((SimpleListWidget.RecipeEntry) object1).getRecipe().getId().toString().compareTo(((SimpleListWidget.RecipeEntry) object2).getRecipe().getId().toString())).collect(Collectors.toList());
    }
}
