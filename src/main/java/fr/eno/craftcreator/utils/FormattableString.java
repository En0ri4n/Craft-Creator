package fr.eno.craftcreator.utils;

public class FormattableString
{
    private final String format;

    private FormattableString(String format)
    {
        this.format = format;
    }

    public String format(Object... args)
    {
        return String.format(format, args);
    }

    public static FormattableString of(String format)
    {
        return new FormattableString(format);
    }
}
