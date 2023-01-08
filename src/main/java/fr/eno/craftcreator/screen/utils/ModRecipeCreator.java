package fr.eno.craftcreator.screen.utils;

import cofh.thermal.core.init.TCoreRecipeTypes;
import fr.eno.craftcreator.kubejs.utils.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import fr.eno.craftcreator.utils.Utilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import vazkii.botania.common.crafting.ModRecipeTypes;

import java.util.List;
import java.util.stream.Stream;

public enum ModRecipeCreator
{
    // Minecraft
    CRAFTING_TABLE(SupportedMods.MINECRAFT, RecipeType.CRAFTING, Utilities.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "crafting_table_recipe_creator.png"), SlotHelper.CRAFTING_TABLE_SLOTS),

    // Botania
    MANA_INFUSION(SupportedMods.BOTANIA, ModRecipeTypes.MANA_INFUSION_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, ModRecipeTypes.ELVEN_TRADE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, ModRecipeTypes.PURE_DAISY_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, ModRecipeTypes.BREW_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, ModRecipeTypes.PETAL_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, ModRecipeTypes.RUNE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, ModRecipeTypes.TERRA_PLATE_TYPE, Utilities.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS),

    // Thermal
    TREE_EXTRACTOR(SupportedMods.THERMAL, TCoreRecipeTypes.MAPPING_TREE_EXTRACTOR, Utilities.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "tree_extractor_recipe_creator.png"), SlotHelper.TREE_EXTRACTOR_SLOTS),
    PULVERIZER(SupportedMods.THERMAL, TCoreRecipeTypes.RECIPE_PULVERIZER, Utilities.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "pulverizer_recipe_creator.png"), SlotHelper.PULVERIZER_SLOTS),
    SAWMILL(SupportedMods.THERMAL, TCoreRecipeTypes.RECIPE_SAWMILL, Utilities.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "sawmill_recipe_creator.png"), SlotHelper.SAWMILL_SLOTS),
    SMELTER(SupportedMods.THERMAL, TCoreRecipeTypes.RECIPE_SMELTER, Utilities.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "smelter_recipe_creator.png"), SlotHelper.SMELTER_SLOTS),
    INSOLATOR(SupportedMods.THERMAL, TCoreRecipeTypes.RECIPE_INSOLATOR, Utilities.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "insolator_recipe_creator.png"), SlotHelper.INSOLATOR_SLOTS);

    private final SupportedMods mod;
    private final RecipeType<?> recipeType;
    private final ResourceLocation guiTexture;
    private final List<PositionnedSlot> slots;

    ModRecipeCreator(SupportedMods mod, RecipeType<?> recipeType, ResourceLocation guiTexture, List<PositionnedSlot> slots)
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

    public static List<ModRecipeCreator> getRecipeCreatorScreens(SupportedMods mod)
    {
        return Stream.of(values()).filter(recipe -> recipe.getMod().equals(mod)).toList();
    }
}