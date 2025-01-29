package fr.eno.craftcreatorapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Identifier
{
    private final String namespace;
    private final String path;

    public static Identifier from(String namespace, String path)
    {
        return new Identifier(namespace, path);
    }

    public static Identifier parse(String id)
    {
        String[] split = id.split(":");
        return new Identifier(split[0], split[1]);
    }
}
