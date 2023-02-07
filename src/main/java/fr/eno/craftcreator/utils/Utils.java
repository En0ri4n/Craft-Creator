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

    /**
     * Generate a string with a given size
     *
     * @param size the size of the string
     * @return the string
     */
    public static String generateString(int size)
    {
        String output = "";

        for(int i = 0; i < size; i++)
        {
            output = output.concat(String.valueOf(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()))));
        }

        return output;
    }
}