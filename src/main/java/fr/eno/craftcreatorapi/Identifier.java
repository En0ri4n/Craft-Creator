package fr.eno.craftcreatorapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Identifier
{
    private final String namespace;
    private final String path;
}
