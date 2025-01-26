package fr.eno.craftcreatorapi;

import java.util.List;

public abstract class SupportedModsBase
{
    public abstract boolean isModLoaded(String modId);

    public abstract List<Identifier> getSupportedRecipeTypes();
}
