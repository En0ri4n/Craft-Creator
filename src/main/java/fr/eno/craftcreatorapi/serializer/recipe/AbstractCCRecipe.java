package fr.eno.craftcreatorapi.serializer.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import fr.eno.craftcreatorapi.utils.FormattableString;
import fr.eno.craftcreatorapi.utils.JsonSerializable;

public abstract class AbstractCCRecipe implements JsonSerializable
{
    protected static final Gson gson = new GsonBuilder().setStrictness(Strictness.LENIENT).create();

    public abstract FormattableString getBaseLine();

    public abstract String toJson();
}