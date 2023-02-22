package fr.eno.craftcreator.recipes.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.eno.craftcreator.utils.FormattableString;

public abstract class ModifiedRecipe
{
    protected static final Gson GSON = new GsonBuilder().setLenient().create();

    public abstract FormattableString getBaseLine();

    public abstract String toJson();
}
