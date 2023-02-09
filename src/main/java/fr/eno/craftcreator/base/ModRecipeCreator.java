package fr.eno.craftcreator.base;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ModRecipeCreator
{
    // Minecraft
    CRAFTING_TABLE(SupportedMods.MINECRAFT, IRecipeType.CRAFTING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "crafting_table_recipe_creator.png"), SlotHelper.CRAFTING_TABLE_SLOTS),
    FURNACE_SMELTING(SupportedMods.MINECRAFT, IRecipeType.SMELTING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_BLASTING(SupportedMods.MINECRAFT, IRecipeType.BLASTING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_SMOKING(SupportedMods.MINECRAFT, IRecipeType.SMOKING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    CAMPFIRE_COOKING(SupportedMods.MINECRAFT, IRecipeType.CAMPFIRE_COOKING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    SMITHING_TABLE(SupportedMods.MINECRAFT, IRecipeType.SMITHING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "smithing_table_recipe_creator.png"), SlotHelper.SMITHING_TABLE_SLOTS),
    STONECUTTER(SupportedMods.MINECRAFT, IRecipeType.STONECUTTING, ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "stonecutter_recipe_creator.png"), SlotHelper.STONECUTTER_SLOTS),

    // Botania
    MANA_INFUSION(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:mana_infusion"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:elven_trade"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:pure_daisy"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:brew"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:petal_apothecary"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:runic_altar"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, CommonUtils.getRecipeTypeByName("botania:terra_plate"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS),

    // Thermal
    TREE_EXTRACTOR(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:tree_extractor"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "tree_extractor_recipe_creator.png"), SlotHelper.TREE_EXTRACTOR_SLOTS),
    PULVERIZER(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:pulverizer"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "pulverizer_recipe_creator.png"), SlotHelper.PULVERIZER_SLOTS),
    SAWMILL(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:sawmill"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "sawmill_recipe_creator.png"), SlotHelper.SAWMILL_SLOTS),
    SMELTER(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:smelter"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "smelter_recipe_creator.png"), SlotHelper.SMELTER_SLOTS),
    INSOLATOR(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:insolator"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "insolator_recipe_creator.png"), SlotHelper.INSOLATOR_SLOTS),
    PRESS(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:press"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "press_recipe_creator.png"), SlotHelper.PRESS_SLOTS),
    FURNACE_THERMAL(SupportedMods.THERMAL, CommonUtils.getRecipeTypeByName("thermal:furnace"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "furnace_recipe_creator.png"), SlotHelper.FURNACE_THERMAL_SLOTS);

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

    public static ModRecipeCreator byName(ResourceLocation recipeTypeName)
    {
        return Stream.of(values()).filter(recipe -> recipeTypeName.equals(CommonUtils.getRecipeTypeName(recipe.getRecipeType()))).findFirst().orElse(null);
    }
    
    public static ModRecipeCreator byRecipeType(IRecipeType<?> recipeType)
    {
        return byName(CommonUtils.getRecipeTypeName(recipeType));
    }
}