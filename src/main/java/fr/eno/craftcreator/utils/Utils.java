package fr.eno.craftcreator.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Encode a string with SHA-256
     *
     * @param toEncode the string to encode
     * @return the encoded string or a random string if an error occurs
     */
    public static String generateSHA256(String toEncode)
    {
        try
        {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(toEncode.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b : digest)
            {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return Utils.generateString(16);
    }
}