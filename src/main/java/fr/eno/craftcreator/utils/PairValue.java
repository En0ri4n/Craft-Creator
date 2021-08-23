package fr.eno.craftcreator.utils;

public class PairValue<K, V>
{
    private final K firstValue;
    private final V secondValue;

    private PairValue(K firstValue, V secondValue)
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

    public static <K, V> PairValue<K, V> create(K firstValue, V secondValue)
    {
        return new PairValue<>(firstValue, secondValue);
    }
}
