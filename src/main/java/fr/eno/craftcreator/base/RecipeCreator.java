package fr.eno.craftcreator.base;

import fr.eno.craftcreator.References;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.FormattableString;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class RecipeCreator
{
    private final SupportedMods mod;
    private final List<PositionnedSlot> slots;
    private final int maxInputSize;
    private final int maxOutputSize;
    private final ResourceLocation recipeTypeLocation;
    private final ResourceLocation itemIcon;
    private final ResourceLocation guiTexture;

    private RecipeCreator(SupportedMods mod, List<PositionnedSlot> slots, int maxInputSize, int maxOutputSize, ResourceLocation recipeTypeLocation, ResourceLocation itemIcon, ResourceLocation guiTexture)
    {
        this.mod = mod;
        this.slots = slots;
        this.maxInputSize = maxInputSize;
        this.maxOutputSize = maxOutputSize;
        this.recipeTypeLocation = recipeTypeLocation;
        this.itemIcon = itemIcon;
        this.guiTexture = guiTexture;
    }

    public SupportedMods getMod()
    {
        return this.mod;
    }

    public <C extends IInventory, T extends IRecipe<C>> IRecipeType<T> getRecipeType()
    {
        return CommonUtils.getRecipeTypeByName(this.recipeTypeLocation);
    }

    public ResourceLocation getRecipeTypeLocation()
    {
        return this.recipeTypeLocation;
    }

    public Item getRecipeIcon()
    {
        Item icon = ForgeRegistries.ITEMS.getValue(this.itemIcon);
        return icon != null && icon != Items.AIR ? icon : Items.COMMAND_BLOCK;
    }

    public ResourceLocation getGuiTexture()
    {
        return guiTexture;
    }

    public List<PositionnedSlot> getSlots()
    {
        return slots;
    }

    public int getMaxInputSize()
    {
        return maxInputSize;
    }

    public int getMaxOutputSize()
    {
        return maxOutputSize;
    }

    public boolean is(RecipeCreator... recipeCreators)
    {
        for(RecipeCreator recipeCreator : recipeCreators)
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
        private ResourceLocation recipeTypeLocation;
        private ResourceLocation itemIcon;
        private ResourceLocation guiTextureName;

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
        public Builder withRecipeType(String recipeType)
        {
            this.recipeTypeLocation = CommonUtils.parse(mod.getModId(), recipeType);
            this.withGuiTexture(GUI_TEXTURE_NAME.format(recipeType));
            return this;
        }

        /**
         * Set the icon of the recipe creator (we assume that the item has the same mod id as the mod)<br>
         * e.g. if the mod id is "craftcreator" and the item is "crafting_table", the item will be "craftcreator:crafting_table"
         *
         * @param item the item of the recipe creator
         * @return the current builder
         */
        public Builder withIcon(String item)
        {
            this.itemIcon = CommonUtils.parse(mod.getModId() + ":" + item);
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
            this.guiTextureName = References.getLoc("textures/gui/container/" + mod.getModId() + "/" + guiTextureName);
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