package fr.eno.craftcreator.recipes.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.eno.craftcreator.utils.FormattableString;
import fr.eno.craftcreator.utils.JsonSerializable;

public abstract class ModifiedRecipe implements JsonSerializable
{
    protected static final Gson GSON = new GsonBuilder().setLenient().create();

    public abstract FormattableString getBaseLine();

    public abstract String toJson();
}
