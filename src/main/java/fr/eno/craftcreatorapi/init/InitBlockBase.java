package fr.eno.craftcreatorapi.init;

import fr.eno.craftcreatorapi.block.CCBlock;

import java.util.ArrayList;
import java.util.List;

public abstract class InitBlockBase
{
    protected final List<CCBlock> blocks = new ArrayList<>();

    protected InitBlockBase()
    {

    }

    public abstract void registerBlocks();
}
