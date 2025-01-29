package fr.eno.craftcreatorapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Feedback
{
    ADDED("craftcreator.serializer.message.recipe.added"),
    REMOVED("craftcreator.serializer.message.recipe.removed"),
    EXISTS("craftcreator.serializer.message.recipe.exists"),
    DONT_EXISTS("craftcreator.serializer.message.recipe.dont_exists"),
    FILE_ERROR("craftcreator.serializer.message.recipe.error");

    private final String messageKey;
}
