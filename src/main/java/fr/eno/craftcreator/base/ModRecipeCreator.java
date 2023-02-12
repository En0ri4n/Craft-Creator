package fr.eno.craftcreator.base;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ModRecipeCreator
{
    // Minecraft
    CRAFTING_TABLE(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:crafting"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "crafting_table_recipe_creator.png"), SlotHelper.CRAFTING_TABLE_SLOTS),
    FURNACE_SMELTING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smelting"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_BLASTING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:blasting"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    FURNACE_SMOKING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smoking"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    CAMPFIRE_COOKING(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:campfire_cooking"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "furnace_recipe_creator.png"), SlotHelper.FURNACE_SLOTS),
    SMITHING_TABLE(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:smithing"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "smithing_table_recipe_creator.png"), SlotHelper.SMITHING_TABLE_SLOTS),
    STONECUTTER(SupportedMods.MINECRAFT, ClientUtils.parse("minecraft:stonecutting"), ClientUtils.getGuiContainerTexture(SupportedMods.MINECRAFT, "stonecutter_recipe_creator.png"), SlotHelper.STONECUTTER_SLOTS),

    // Botania
    MANA_INFUSION(SupportedMods.BOTANIA, ClientUtils.parse("botania:mana_infusion"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "mana_infusion_recipe_creator.png"), SlotHelper.MANA_INFUSION_SLOTS),
    ELVEN_TRADE(SupportedMods.BOTANIA, ClientUtils.parse("botania:elven_trade"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "elven_trade_recipe_creator.png"), SlotHelper.ELVEN_TRADE_SLOTS),
    PURE_DAISY(SupportedMods.BOTANIA, ClientUtils.parse("botania:pure_daisy"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "pure_daisy_recipe_creator.png"), SlotHelper.PURE_DAISY_SLOTS),
    BREWERY(SupportedMods.BOTANIA, ClientUtils.parse("botania:brew"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "brewery_recipe_creator.png"), SlotHelper.BREWERY_SLOTS),
    PETAL_APOTHECARY(SupportedMods.BOTANIA, ClientUtils.parse("botania:petal_apothecary"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "petal_apothecary_recipe_creator.png"), SlotHelper.PETAL_APOTHECARY_SLOTS),
    RUNIC_ALTAR(SupportedMods.BOTANIA, ClientUtils.parse("botania:runic_altar"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "runic_altar_recipe_creator.png"), SlotHelper.RUNIC_ALTAR_SLOTS),
    TERRA_PLATE(SupportedMods.BOTANIA, ClientUtils.parse("botania:terra_plate"), ClientUtils.getGuiContainerTexture(SupportedMods.BOTANIA, "terra_plate_recipe_creator.png"), SlotHelper.TERRA_PLATE_SLOTS),

    // Thermal
    TREE_EXTRACTOR(SupportedMods.THERMAL, ClientUtils.parse("thermal:tree_extractor"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "tree_extractor_recipe_creator.png"), SlotHelper.TREE_EXTRACTOR_SLOTS),
    PULVERIZER(SupportedMods.THERMAL, ClientUtils.parse("thermal:pulverizer"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "pulverizer_recipe_creator.png"), SlotHelper.PULVERIZER_SLOTS),
    SAWMILL(SupportedMods.THERMAL, ClientUtils.parse("thermal:sawmill"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "sawmill_recipe_creator.png"), SlotHelper.SAWMILL_SLOTS),
    SMELTER(SupportedMods.THERMAL, ClientUtils.parse("thermal:smelter"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "smelter_recipe_creator.png"), SlotHelper.SMELTER_SLOTS),
    INSOLATOR(SupportedMods.THERMAL, ClientUtils.parse("thermal:insolator"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "insolator_recipe_creator.png"), SlotHelper.INSOLATOR_SLOTS),
    PRESS(SupportedMods.THERMAL, ClientUtils.parse("thermal:press"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "press_recipe_creator.png"), SlotHelper.PRESS_SLOTS),
    FURNACE_THERMAL(SupportedMods.THERMAL, ClientUtils.parse("thermal:furnace"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "furnace_recipe_creator.png"), SlotHelper.FURNACE_THERMAL_SLOTS),
    CENTRIFUGE(SupportedMods.THERMAL, ClientUtils.parse("thermal:centrifuge"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "centrifuge_recipe_creator.png"), SlotHelper.CENTRIFUGE_SLOTS),
    CHILLER(SupportedMods.THERMAL, ClientUtils.parse("thermal:chiller"), ClientUtils.getGuiContainerTexture(SupportedMods.THERMAL, "chiller_recipe_creator.png"), SlotHelper.CHILLER_SLOTS),;

    private final SupportedMods mod;
    private final ResourceLocation recipeTypeLocation;
    private final ResourceLocation guiTexture;
    private final List<PositionnedSlot> slots;

    ModRecipeCreator(SupportedMods mod, ResourceLocation recipeTypeLocation, ResourceLocation guiTexture, List<PositionnedSlot> slots)
    {
        this.mod = mod;
        this.recipeTypeLocation = recipeTypeLocation;
        this.guiTexture = guiTexture;
        this.slots = slots;
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