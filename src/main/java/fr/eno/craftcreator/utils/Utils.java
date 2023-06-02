package fr.eno.craftcreator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class Utils
{
    public static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * Verify that the object is not null.<br>
     * Calls {@link Objects#requireNonNull(Object)} with the object
     *
     * @param obj The object to verify
     * @param <T> the type of the object
     * @return the object
     * @see Objects#requireNonNull(Object)
     */
    public static <T> T notNull(T obj)
    {
        return Objects.requireNonNull(obj);
    }
}