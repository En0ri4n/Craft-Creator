package fr.eno.craftcreator.utils;

import java.util.Objects;
import java.util.Random;

public class Utils
{
    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

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