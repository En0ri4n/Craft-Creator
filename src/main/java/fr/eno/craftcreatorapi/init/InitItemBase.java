package fr.eno.craftcreatorapi.init;

import fr.eno.craftcreatorapi.item.CCItem;

import java.util.ArrayList;
import java.util.List;

public abstract class InitItemBase
{
    protected final List<CCItem> items = new ArrayList<>();

    protected InitItemBase()
    {

    }

    public abstract void registerItems();
}
