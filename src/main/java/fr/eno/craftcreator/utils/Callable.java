package fr.eno.craftcreator.utils;

/**
 * Simple interface with a run function
 *
 * @param <V> The type of the return value
 */
public interface Callable<V>
{
    void run(V v);
}
