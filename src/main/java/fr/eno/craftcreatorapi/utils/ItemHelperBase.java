package fr.eno.craftcreatorapi.utils;

import java.util.List;

public abstract class ItemHelperBase<I extends HasRegistryName>
{
    public abstract I getItem(Identifier id);

    public abstract List<I> getItems();
}
