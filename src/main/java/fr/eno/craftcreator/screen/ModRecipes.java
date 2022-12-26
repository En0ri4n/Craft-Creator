package fr.eno.craftcreator.screen;

import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import vazkii.botania.common.crafting.ModRecipeTypes;

import java.util.List;
import java.util.stream.Stream;

public enum ModRecipes
{
    MANA_INFUSION(SupportedMods.BOTANIA, ModRecipeTypes.MANA_INFUSION_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, ModRecipeTypes.ELVEN_TRADE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, ModRecipeTypes.PURE_DAISY_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, ModRecipeTypes.BREW_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, ModRecipeTypes.PETAL_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, ModRecipeTypes.RUNE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, ModRecipeTypes.TERRA_PLATE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS);

    private final SupportedMods mod;
    private final RecipeType<?> recipeType;
    private final ResourceLocation guiTexture;
    private final List<PositionnedSlot> slots;

    ModRecipes(SupportedMods mod, RecipeType<?> recipeType, ResourceLocation guiTexture, List<PositionnedSlot> slots)
    {
        this.mod = mod;
        this.recipeType = recipeType;
        this.guiTexture = guiTexture;
        this.slots = slots;
    }

    public SupportedMods getMod()
    {
        return this.mod;
    }

    public RecipeType<?> getRecipeType()
    {
        return recipeType;
    }

    public ResourceLocation getGuiTexture()
    {
        return guiTexture;
    }

    public List<PositionnedSlot> getSlots()
    {
        return slots;
    }

    public static List<ModRecipes> getRecipes(SupportedMods mod)
    {
        return Stream.of(values()).filter(recipe -> recipe.getMod().equals(mod)).toList();
    }
}