package fr.eno.craftcreatorapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback
{
    ADDED("serializer.message.recipe.added"),
    REMOVED("serializer.message.recipe.removed"),
    EXISTS("serializer.message.recipe.exists"),
    DONT_EXISTS("serializer.message.recipe.dont_exists"),
    FILE_ERROR("serializer.message.recipe.error");

    private final String messageKey;
}
