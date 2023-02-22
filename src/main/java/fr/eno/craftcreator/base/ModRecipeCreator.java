package fr.eno.craftcreator.base;


import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ModRecipeCreator
{
    // Minecraft
    CRAFTING_TABLE(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:crafting"), "crafting_table_recipe_creator.png", SlotHelper.CRAFTING_TABLE_SLOTS),
    FURNACE_SMELTING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smelting"), "furnace_recipe_creator.png", SlotHelper.FURNACE_SLOTS),
    FURNACE_BLASTING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:blasting"), "furnace_recipe_creator.png", SlotHelper.FURNACE_SLOTS),
    FURNACE_SMOKING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smoking"), "furnace_recipe_creator.png", SlotHelper.FURNACE_SLOTS),
    CAMPFIRE_COOKING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:campfire_cooking"), "furnace_recipe_creator.png", SlotHelper.FURNACE_SLOTS),
    SMITHING_TABLE(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smithing"), "smithing_table_recipe_creator.png", SlotHelper.SMITHING_TABLE_SLOTS),
    STONECUTTER(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:stonecutting"), "stonecutter_recipe_creator.png", SlotHelper.STONECUTTER_SLOTS),

    // Botania
    MANA_INFUSION(SupportedMods.BOTANIA, ClientUtils.parse("botania:mana_infusion"), "mana_infusion_recipe_creator.png", SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, ClientUtils.parse("botania:elven_trade"), "elven_trade_recipe_creator.png", SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, ClientUtils.parse("botania:pure_daisy"), "pure_daisy_recipe_creator.png", SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, ClientUtils.parse("botania:brew"), "brewery_recipe_creator.png", SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, ClientUtils.parse("botania:petal_apothecary"), "petal_apothecary_recipe_creator.png", SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, ClientUtils.parse("botania:runic_altar"), "runic_altar_recipe_creator.png", SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, ClientUtils.parse("botania:terra_plate"), "terra_plate_recipe_creator.png", SlotHelper.TERRA_PLATE_SLOTS),

    // Thermal
    TREE_EXTRACTOR(SupportedMods.THERMAL, ClientUtils.parse("thermal:tree_extractor"), "tree_extractor_recipe_creator.png", SlotHelper.TREE_EXTRACTOR_SLOTS),
    PULVERIZER(SupportedMods.THERMAL, ClientUtils.parse("thermal:pulverizer"), "pulverizer_recipe_creator.png", SlotHelper.PULVERIZER_SLOTS),
    SAWMILL(SupportedMods.THERMAL, ClientUtils.parse("thermal:sawmill"), "sawmill_recipe_creator.png", SlotHelper.SAWMILL_SLOTS),
    SMELTER(SupportedMods.THERMAL, ClientUtils.parse("thermal:smelter"), "smelter_recipe_creator.png", SlotHelper.SMELTER_SLOTS),
    INSOLATOR(SupportedMods.THERMAL, ClientUtils.parse("thermal:insolator"), "insolator_recipe_creator.png", SlotHelper.INSOLATOR_SLOTS),
    PRESS(SupportedMods.THERMAL, ClientUtils.parse("thermal:press"), "press_recipe_creator.png", SlotHelper.PRESS_SLOTS),
    FURNACE_THERMAL(SupportedMods.THERMAL, ClientUtils.parse("thermal:furnace"), "furnace_recipe_creator.png", SlotHelper.FURNACE_THERMAL_SLOTS),
    CENTRIFUGE(SupportedMods.THERMAL, ClientUtils.parse("thermal:centrifuge"), "centrifuge_recipe_creator.png", SlotHelper.CENTRIFUGE_SLOTS),
    CHILLER(SupportedMods.THERMAL, ClientUtils.parse("thermal:chiller"), "chiller_recipe_creator.png", SlotHelper.CHILLER_SLOTS),
    CRUCIBLE(SupportedMods.THERMAL, ClientUtils.parse("thermal:crucible"), "crucible_recipe_creator.png", SlotHelper.CRUCIBLE_SLOTS),
    REFINERY(SupportedMods.THERMAL, ClientUtils.parse("thermal:refinery"), "refinery_recipe_creator.png", SlotHelper.REFINERY_SLOTS),
    BOTTLER(SupportedMods.THERMAL, ClientUtils.parse("thermal:bottler"), "bottler_recipe_creator.png", SlotHelper.BOTTLER_SLOTS),
    PYROLYZER(SupportedMods.THERMAL, ClientUtils.parse("thermal:pyrolyzer"), "pyrolyzer_recipe_creator.png", SlotHelper.PYROLYZER_SLOTS),

    // Create
    CRUSHING(SupportedMods.CREATE, ClientUtils.parse("create:crushing"), "crushing_recipe_creator.png", SlotHelper.CRUSHING_SLOTS),
    CUTTING(SupportedMods.CREATE, ClientUtils.parse("create:cutting"), "cutting_recipe_creator.png", SlotHelper.CUTTING_SLOTS),;

    private final SupportedMods mod;
    private final ResourceLocation recipeTypeLocation;
    private final String guiTextureName;
    private final List<PositionnedSlot> slots;

    ModRecipeCreator(SupportedMods mod, ResourceLocation recipeTypeLocation, String guiTextureName, List<PositionnedSlot> slots)
    {
        this.mod = mod;
        this.recipeTypeLocation = recipeTypeLocation;
        this.guiTextureName = guiTextureName;
        this.slots = slots;
    }

    public SupportedMods getMod()
    {
        return this.mod;
    }

    public <C extends Container, T extends Recipe<C>> RecipeType<T> getRecipeType()
    {
        return CommonUtils.getRecipeTypeByName(this.recipeTypeLocation);
    }

    public ResourceLocation getRecipeTypeLocation()
    {
        return this.recipeTypeLocation;
    }

    public ResourceLocation getGuiTexture()
    {
        return ClientUtils.getGuiContainerTexture(getMod(), guiTextureName);
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
    
    public static ModRecipeCreator byRecipeType(RecipeType<?> recipeType)
    {
        return byName(CommonUtils.getRecipeTypeName(recipeType));
    }
}