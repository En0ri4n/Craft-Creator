package fr.eno.craftcreatorapi;

import fr.eno.craftcreatorapi.utils.Identifier;

public interface ReferenceBase
{
    String getModId();
    String getModName();
    String getModVersion();
    Identifier getTranslation(String key);
}
