package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.utils.Translatable;
import net.minecraft.util.text.IFormattableTextComponent;

public enum EntryType implements Translatable
{
    ITEM("entry.type.item"),
    FLUID("entry.type.fluid"),
    TAG("entry.type.tag"),
    OTHER("entry.type.other");

    private final String translationKey;

    EntryType(String translationKey)
    {
        this.translationKey = translationKey;
    }

    @Override
    public IFormattableTextComponent getTranslationComponent()
    {
        return References.getTranslate(this.translationKey);
    }

    public boolean isTag()
    {
        return this == TAG;
    }

    public boolean isFluid()
    {
        return this == FLUID;
    }

    public boolean isItem()
    {
        return this == ITEM;
    }

    public boolean isOther()
    {
        return this == OTHER;
    }
}
