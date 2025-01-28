package fr.eno.craftcreatorapi.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeDescriptors
{
    INPUT_ITEM("input_item"),
    OUTPUT_ITEM("output_item"),
    MOD_ID("mod_id"),
    RECIPE_TYPE("recipe_type"),
    RECIPE_ID("recipe_id");

    private final String tag;

    public static RecipeDescriptors byTag(String tag)
    {
        for(RecipeDescriptors descriptor : values())
            if(descriptor.getTag().equals(tag)) return descriptor;

        return null;
    }
}