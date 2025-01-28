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
}
