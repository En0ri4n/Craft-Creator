package fr.eno.craftcreator.screen.utils;

import cofh.thermal.core.init.TCoreRecipeTypes;
import fr.eno.craftcreator.recipes.utils.SupportedMods;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import fr.eno.craftcreator.utils.Utils;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.crafting.ModRecipeTypes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ModRecipeCreator
{
    // Minecraft
    CRAFTING_TABLE(SupportedMods.MINECRAFT, IRecipeType.CRAFTING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "crafting_table_recipe_creator.png"), SlotHelper.CRAFTING_TABLE_SLOTS),
    FURNACE_SMELTING(SupportedMods.MINECRAFT, IRecipeType.SMELTING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_BLASTING(SupportedMods.MINECRAFT, IRecipeType.BLASTING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_SMOKING(SupportedMods.MINECRAFT, IRecipeType.SMOKING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    CAMPFIRE_COOKING(SupportedMods.MINECRAFT, IRecipeType.CAMPFIRE_COOKING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    SMITHING_TABLE(SupportedMods.MINECRAFT, IRecipeType.SMITHING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "smithing_table_recipe_creator.png"), SlotHelper.SMITHING_TABLE_SLOTS),
    STONECUTTER(SupportedMods.MINECRAFT, IRecipeType.STONECUTTING, Utils.getGuiContainerTexture(SupportedMods.MINECRAFT.getModId(), "stonecutter_recipe_creator.png"), SlotHelper.STONECUTTER_SLOTS),

    // Botania
    MANA_INFUSION(SupportedMods.BOTANIA, byName("botania:mana_infusion"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, byName("botania:elven_trade"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, byName("botania:pure_daisy"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, byName("botania:brew"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, byName("botania:petal_apothecary"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, byName("botania:runic_altar"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, byName("botania:terra_plate"), Utils.getGuiContainerTexture(SupportedMods.BOTANIA.getModId(), "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS),

    // Thermal
    TREE_EXTRACTOR(SupportedMods.THERMAL, byName("thermal:tree_extractor"), Utils.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "tree_extractor_recipe_creator.png"), SlotHelper.TREE_EXTRACTOR_SLOTS),
    PULVERIZER(SupportedMods.THERMAL, byName("thermal:pulverizer"), Utils.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "pulverizer_recipe_creator.png"), SlotHelper.PULVERIZER_SLOTS),
    SAWMILL(SupportedMods.THERMAL, byName("thermal:sawmill"), Utils.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "sawmill_recipe_creator.png"), SlotHelper.SAWMILL_SLOTS),
    SMELTER(SupportedMods.THERMAL, byName("thermal:smelter"), Utils.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "smelter_recipe_creator.png"), SlotHelper.SMELTER_SLOTS),
    INSOLATOR(SupportedMods.THERMAL, byName("thermal:insolator"), Utils.getGuiContainerTexture(SupportedMods.THERMAL.getModId(), "insolator_recipe_creator.png"), SlotHelper.INSOLATOR_SLOTS);

    private final SupportedMods mod;
    private final IRecipeType<?> recipeType;
    private final ResourceLocation guiTexture;
    private final List<PositionnedSlot> slots;

    ModRecipeCreator(SupportedMods mod, IRecipeType<?> recipeType, ResourceLocation guiTexture, List<PositionnedSlot> slots)
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

    public IRecipeType<?> getRecipeType()
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
        return Stream.of(values()).filter(recipe -> recipe.getMod().equals(mod)).collect(Collectors.toList());
    }

    private static IRecipeType<?> byName(String resourceLocation)
    {
        return Registry.RECIPE_TYPE.getOptional(ResourceLocation.tryParse(resourceLocation)).orElse(null);
    }
}