package fr.eno.craftcreator.base;

import fr.eno.craftcreator.utils.SlotHelper;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModRecipeCreators
{
    public static final ArrayList<RecipeCreator> RECIPE_CREATORS = new ArrayList<>();

    // Minecraft
    public static final RecipeCreator CRAFTING_TABLE;
    public static final RecipeCreator FURNACE_SMELTING;
    public static final RecipeCreator FURNACE_BLASTING;
    public static final RecipeCreator FURNACE_SMOKING;
    public static final RecipeCreator CAMPFIRE_COOKING;
    public static final RecipeCreator SMITHING;
    public static final RecipeCreator STONECUTTING;

    // Botania
    public static final RecipeCreator MANA_INFUSION;
    public static final RecipeCreator ELVEN_TRADE;
    public static final RecipeCreator PURE_DAISY;
    public static final RecipeCreator BREWERY;
    public static final RecipeCreator PETAL_APOTHECARY;
    public static final RecipeCreator RUNIC_ALTAR;
    public static final RecipeCreator TERRA_PLATE;

    // Thermal
    public static final RecipeCreator TREE_EXTRACTOR;
    public static final RecipeCreator PULVERIZER;
    public static final RecipeCreator SAWMILL;
    public static final RecipeCreator SMELTER;
    public static final RecipeCreator INSOLATOR;
    public static final RecipeCreator PRESS;
    public static final RecipeCreator THERMAL_FURNACE;
    public static final RecipeCreator CENTRIFUGE;
    public static final RecipeCreator CHILLER;
    public static final RecipeCreator CRUCIBLE;
    public static final RecipeCreator REFINERY;
    public static final RecipeCreator BOTTLER;
    public static final RecipeCreator PYROLYZER;

    // Create
    public static final RecipeCreator CRUSHING;
    public static final RecipeCreator CUTTING;

    public static List<RecipeCreator> getRecipeCreatorScreens(SupportedMods mod)
    {
        return RECIPE_CREATORS.stream().filter(recipe -> recipe.getMod().equals(mod)).collect(Collectors.toList());
    }

    public static RecipeCreator byName(ResourceLocation recipeTypeName)
    {
        return RECIPE_CREATORS.stream().filter(recipe -> recipeTypeName.equals(recipe.getRecipeTypeLocation())).findFirst().orElse(null);
    }

    private static RecipeCreator register(RecipeCreator.Builder builder)
    {
        RecipeCreator recipe = builder.build();
        RECIPE_CREATORS.add(recipe);
        return recipe;
    }

    static
    {
        CRAFTING_TABLE = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("crafting").withIcon("crafting_table").withSlots(SlotHelper.CRAFTING_TABLE_SLOTS));
        FURNACE_SMELTING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("smelting").withIcon("furnace").withSlots(SlotHelper.FURNACE_SLOTS).withGuiTexture("furnace_recipe_creator.png"));
        FURNACE_BLASTING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("blasting").withIcon("blast_furnace").withSlots(SlotHelper.FURNACE_SLOTS).withGuiTexture("furnace_recipe_creator.png"));
        FURNACE_SMOKING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("smoking").withIcon("smoker").withSlots(SlotHelper.FURNACE_SLOTS).withGuiTexture("furnace_recipe_creator.png"));
        CAMPFIRE_COOKING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("campfire_cooking").withIcon("campfire").withSlots(SlotHelper.FURNACE_SLOTS).withGuiTexture("furnace_recipe_creator.png"));
        STONECUTTING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("stonecutting").withIcon("stonecutter").withSlots(SlotHelper.STONECUTTING_SLOTS));
        SMITHING = register(RecipeCreator.Builder.of(SupportedMods.MINECRAFT).withRecipeType("smithing").withIcon("smithing_table").withSlots(SlotHelper.SMITHING_SLOTS));

        // Botania
        MANA_INFUSION = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("mana_infusion").withIcon("creative_pool").withSlots(SlotHelper.MANA_INFUSION_SLOTS));
        ELVEN_TRADE = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("elven_trade").withIcon("glimmering_livingwood").withSlots(SlotHelper.ELVEN_TRADE_SLOTS));
        PURE_DAISY = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("pure_daisy").withIcon("pure_daisy").withSlots(SlotHelper.PURE_DAISY_SLOTS));
        BREWERY = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("brewery").withIcon("brewery").withSlots(SlotHelper.BREWERY_SLOTS));
        PETAL_APOTHECARY = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("petal_apothecary").withIcon("apothecary_default").withSlots(SlotHelper.PETAL_APOTHECARY_SLOTS));
        RUNIC_ALTAR = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("rune_altar").withIcon("runic_altar").withSlots(SlotHelper.RUNIC_ALTAR_SLOTS));
        TERRA_PLATE = register(RecipeCreator.Builder.of(SupportedMods.BOTANIA).withRecipeType("terra_plate").withIcon("terra_plate").withSlots(SlotHelper.TERRA_PLATE_SLOTS));

        // Thermal
        TREE_EXTRACTOR = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("tree_extractor").withIcon("device_tree_extractor").withSlots(SlotHelper.TREE_EXTRACTOR_SLOTS));
        PULVERIZER = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("pulverizer").withIcon("machine_pulverizer").withSlots(SlotHelper.PULVERIZER_SLOTS));
        SAWMILL = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("sawmill").withIcon("machine_sawmill").withSlots(SlotHelper.SAWMILL_SLOTS));
        SMELTER = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("smelter").withIcon("machine_smelter").withSlots(SlotHelper.SMELTER_SLOTS));
        INSOLATOR = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("insolator").withIcon("machine_insolator").withSlots(SlotHelper.INSOLATOR_SLOTS));
        PRESS = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("press").withIcon("machine_press").withSlots(SlotHelper.PRESS_SLOTS));
        THERMAL_FURNACE = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("furnace").withIcon("machine_furnace").withSlots(SlotHelper.THERMAL_FURNACE_SLOTS));
        CENTRIFUGE = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("centrifuge").withIcon("machine_centrifuge").withSlots(SlotHelper.CENTRIFUGE_SLOTS));
        CHILLER = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("chiller").withIcon("machine_chiller").withSlots(SlotHelper.CHILLER_SLOTS));
        CRUCIBLE = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("crucible").withIcon("machine_crucible").withSlots(SlotHelper.CRUCIBLE_SLOTS));
        REFINERY = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("refinery").withIcon("machine_refinery").withSlots(SlotHelper.REFINERY_SLOTS));
        BOTTLER = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("bottler").withIcon("machine_bottler").withSlots(SlotHelper.BOTTLER_SLOTS));
        PYROLYZER = register(RecipeCreator.Builder.of(SupportedMods.THERMAL).withRecipeType("pyrolyzer").withIcon("machine_pyrolyzer").withSlots(SlotHelper.PYROLYZER_SLOTS));

        // Create
        CRUSHING = register(RecipeCreator.Builder.of(SupportedMods.CREATE).withRecipeType("crushing").withIcon("crushing_wheel").withSlots(SlotHelper.CRUSHING_SLOTS).setSize(1, -1).withGuiTexture("create_recipe_creator.png"));
        CUTTING = register(RecipeCreator.Builder.of(SupportedMods.CREATE).withRecipeType("cutting").withIcon("mechanical_saw").withSlots(SlotHelper.CUTTING_SLOTS).setSize(1, -1).withGuiTexture("create_recipe_creator.png"));

        RECIPE_CREATORS.trimToSize(); // Trim to size to save memory
    }
}
