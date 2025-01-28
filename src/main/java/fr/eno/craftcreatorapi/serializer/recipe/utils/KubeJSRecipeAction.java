package fr.eno.craftcreatorapi.serializer.recipe.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KubeJSRecipeAction
{
    REMOVED("remove"),
    REPLACED_INPUT("replaceInput"),
    REPLACED_OUTPUT("replaceOutput");

    public static final String CUSTOM = "custom";
    private final String descriptor;

    public static KubeJSRecipeAction byDescriptor(String descriptor)
    {
        for(KubeJSRecipeAction type : KubeJSRecipeAction.values())
        {
            if(type.getDescriptor().equals(descriptor)) return type;
        }

        return null;
    }

    public static boolean isKubeJSRecipeType(String descriptor)
    {
        return byDescriptor(descriptor) != null;
    }
}