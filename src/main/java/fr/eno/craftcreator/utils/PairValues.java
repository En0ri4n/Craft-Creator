package fr.eno.craftcreator.utils;

public class PairValues<K, V>
{
    private final K firstValue;
    private final V secondValue;

    private PairValues(K firstValue, V secondValue)
    {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public K getFirstValue()
    {
        return firstValue;
    }

    public V getSecondValue()
    {
        return secondValue;
    }

    public static <K, V> PairValues<K, V> create(K firstValue, V secondValue)
    {
        return new PairValues<>(firstValue, secondValue);
    }
}
