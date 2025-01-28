package fr.eno.craftcreatorapi;


import fr.eno.craftcreatorapi.container.slot.PositionnedSlot;
import fr.eno.craftcreatorapi.utils.FormattableString;
import fr.eno.craftcreatorapi.utils.Identifier;
import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public abstract class AbstractRecipeCreator
{
    private final SupportedMods mod;
    private final List<PositionnedSlot> slots;
    private final int maxInputSize;
    private final int maxOutputSize;
    private final Identifier recipeTypeLocation;
    private final Identifier itemIcon;
    private final Identifier guiTexture;

    private AbstractRecipeCreator(SupportedMods mod, List<PositionnedSlot> slots, int maxInputSize, int maxOutputSize, Identifier recipeTypeLocation, Identifier itemIcon, Identifier guiTexture)
    {
        this.mod = mod;
        this.slots = slots;
        this.maxInputSize = maxInputSize;
        this.maxOutputSize = maxOutputSize;
        this.recipeTypeLocation = recipeTypeLocation;
        this.itemIcon = itemIcon;
        this.guiTexture = guiTexture;
    }

    public <C extends Container, T extends Recipe<C>> RecipeType<T> getRecipeType()
    {
        return CommonUtils.getRecipeTypeByName(this.recipeTypeLocation);
    }

    public Item getRecipeIcon()
    {
        Item icon = ForgeRegistries.ITEMS.getValue(this.itemIcon);
        return icon != null && icon != Items.AIR ? icon : Items.COMMAND_BLOCK;
    }

    public boolean is(AbstractRecipeCreator... recipeCreators)
    {
        for(AbstractRecipeCreator recipeCreator : recipeCreators)
            if(this.equals(recipeCreator))
                return true;

        return false;
    }

    public static class Builder
    {
        private static final FormattableString GUI_TEXTURE_NAME = FormattableString.of("%s_recipe_creator.png");

        private final SupportedMods mod;
        private List<PositionnedSlot> slots;
        private int maxInputSize = -1;
        private int maxOutputSize = -1;
        private Identifier recipeTypeLocation;
        private Identifier itemIcon;
        private Identifier guiTextureName;

        private Builder(SupportedMods mod)
        {
            this.mod = mod;
        }

        /**
         * Set the slots of the recipe creator
         *
         * @param slots the slots of the recipe creator
         * @return the current builder
         */
        public Builder withSlots(List<PositionnedSlot> slots)
        {
            this.slots = slots;
            return this;
        }

        /**
         * Set the max input/output size of the recipe creator (default: -1)<br>
         * Used for Create recipe creator to setup system of input/output
         *
         * @param maxInputSize the max input size of the recipe creator
         * @return the current builder
         */
        public Builder setSize(int maxInputSize, int maxOutputSize)
        {
            this.maxInputSize = maxInputSize;
            this.maxOutputSize = maxOutputSize;
            return this;
        }

        /**
         * Set the recipe type of the recipe creator (we assume that the recipe type has the same mod id as the mod).<br>
         * If the mod id is "craftcreator" and the recipe type is "crafting", the recipe type will be "craftcreator:crafting".<br>
         * Also set the gui texture of the recipe creator with the form: [recipe_type]_recipe_creator.png (can be override with {@link #withGuiTexture(String)})
         *
         * @param recipeType the recipe type of the recipe creator
         * @return the current builder
         */
        public Builder withRecipeType(Identifier recipeType)
        {
            this.recipeTypeLocation = recipeType;
            this.withGuiTexture(GUI_TEXTURE_NAME.format(recipeType.getPath()));
            return this;
        }

        /**
         * Set the icon of the recipe creator (we assume that the item has the same mod id as the mod)<br>
         * e.g. if the mod id is "craftcreator" and the item is "crafting_table", the item will be "craftcreator:crafting_table"
         *
         * @param icon the icon of the recipe creator
         * @return the current builder
         */
        public Builder withIcon(Identifier icon)
        {
            this.itemIcon = icon;
            return this;
        }

        /**
         * The gui texture of the recipe creator
         *
         * @param guiTextureName the gui texture of the recipe creator
         * @return the current builder
         */
        public Builder withGuiTexture(String guiTextureName)
        {
            this.guiTextureName = CraftCreatorAPI.getInstance().getReferences().getTranslation("textures/gui/container/" + mod.getModId() + "/" + guiTextureName);
            return this;
        }

        /**
         * Build the recipe creator, make sure that you have set all the fields
         *
         * @return the built recipe creator
         */
        public RecipeCreator build()
        {
            return new RecipeCreator(mod, slots, maxInputSize, maxOutputSize, recipeTypeLocation, itemIcon, guiTextureName);
        }

        public static Builder of(SupportedMods mod)
        {
            return new Builder(mod);
        }
    }
}