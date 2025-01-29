package fr.eno.craftcreatorapi;

import fr.eno.craftcreatorapi.utils.Identifier;

public abstract class ReferenceBase
{
    public static final String MOD_ID = "craftcreator";
    abstract String getModName();
    abstract String getModVersion();

    public Identifier getIdentifier(String path)
    {
        return new Identifier(MOD_ID, path);
    }

    abstract String getTranslation(String key);
}
