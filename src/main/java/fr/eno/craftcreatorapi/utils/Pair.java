package fr.eno.craftcreatorapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<K, V>
{
    private final K firstValue;
    private final V secondValue;

    public static <K, V> Pair<K, V> create(K firstValue, V secondValue)
    {
        return new Pair<>(firstValue, secondValue);
    }
}
