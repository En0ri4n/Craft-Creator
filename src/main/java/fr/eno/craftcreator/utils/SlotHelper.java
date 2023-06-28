package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.container.slot.utils.DefinedPositionnedSlot;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BucketItem;
import vazkii.botania.api.brew.IBrewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlotHelper
{
    // Minecraft
    public static final int MINECRAFT_SLOTS_SIZE;
    public static final List<PositionnedSlot> MINECRAFT_SLOTS;
    public static final List<PositionnedSlot> MINECRAFT_SLOTS_INPUT;
    public static final List<PositionnedSlot> MINECRAFT_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS_INPUT;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> FURNACE_SLOTS;
    public static final List<PositionnedSlot> FURNACE_SLOTS_INPUT;
    public static final List<PositionnedSlot> FURNACE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> SMITHING_SLOTS;
    public static final List<PositionnedSlot> SMITHING_SLOTS_INPUT;
    public static final List<PositionnedSlot> SMITHING_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> STONECUTTING_SLOTS;
    public static final List<PositionnedSlot> STONECUTTING_SLOTS_INPUT;
    public static final List<PositionnedSlot> STONECUTTING_SLOTS_OUTPUT;

    // Botania
    public static final int BOTANIA_SLOTS_SIZE;
    public static final List<PositionnedSlot> BOTANIA_SLOTS;
    public static final List<PositionnedSlot> BOTANIA_SLOTS_INPUT;
    public static final List<PositionnedSlot> BOTANIA_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> MANA_INFUSION_SLOTS;
    public static final List<PositionnedSlot> MANA_INFUSION_SLOTS_INPUT;
    public static final List<PositionnedSlot> MANA_INFUSION_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> ELVEN_TRADE_SLOTS;
    public static final List<PositionnedSlot> ELVEN_TRADE_SLOTS_INPUT;
    public static final List<PositionnedSlot> ELVEN_TRADE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> PURE_DAISY_SLOTS;
    public static final List<PositionnedSlot> PURE_DAISY_SLOTS_INPUT;
    public static final List<PositionnedSlot> PURE_DAISY_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> BREWERY_SLOTS;
    public static final List<PositionnedSlot> BREWERY_SLOTS_INPUT;
    public static final List<PositionnedSlot> BREWERY_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> PETAL_APOTHECARY_SLOTS;
    public static final List<PositionnedSlot> PETAL_APOTHECARY_SLOTS_INPUT;
    public static final List<PositionnedSlot> PETAL_APOTHECARY_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> RUNIC_ALTAR_SLOTS;
    public static final List<PositionnedSlot> RUNIC_ALTAR_SLOTS_INPUT;
    public static final List<PositionnedSlot> RUNIC_ALTAR_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> TERRA_PLATE_SLOTS;
    public static final List<PositionnedSlot> TERRA_PLATE_SLOTS_INPUT;
    public static final List<PositionnedSlot> TERRA_PLATE_SLOTS_OUTPUT;

    // Thermal
    public static final int THERMAL_SLOTS_SIZE;
    public static final List<PositionnedSlot> THERMAL_SLOTS;
    public static final List<PositionnedSlot> THERMAL_SLOTS_INPUT;
    public static final List<PositionnedSlot> THERMAL_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> TREE_EXTRACTOR_SLOTS;
    public static final List<PositionnedSlot> TREE_EXTRACTOR_SLOTS_INPUT;
    public static final List<PositionnedSlot> TREE_EXTRACTOR_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> PULVERIZER_SLOTS;
    public static final List<PositionnedSlot> PULVERIZER_SLOTS_INPUT;
    public static final List<PositionnedSlot> PULVERIZER_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> SAWMILL_SLOTS;
    public static final List<PositionnedSlot> SAWMILL_SLOTS_INPUT;
    public static final List<PositionnedSlot> SAWMILL_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> SMELTER_SLOTS;
    public static final List<PositionnedSlot> SMELTER_SLOTS_INPUT;
    public static final List<PositionnedSlot> SMELTER_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> INSOLATOR_SLOTS;
    public static final List<PositionnedSlot> INSOLATOR_SLOTS_INPUT;
    public static final List<PositionnedSlot> INSOLATOR_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> PRESS_SLOTS;
    public static final List<PositionnedSlot> PRESS_SLOTS_INPUT;
    public static final List<PositionnedSlot> PRESS_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> THERMAL_FURNACE_SLOTS;
    public static final List<PositionnedSlot> THERMAL_FURNACE_SLOTS_INPUT;
    public static final List<PositionnedSlot> THERMAL_FURNACE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS_INPUT;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CHILLER_SLOTS;
    public static final List<PositionnedSlot> CHILLER_SLOTS_INPUT;
    public static final List<PositionnedSlot> CHILLER_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CRUCIBLE_SLOTS;
    public static final List<PositionnedSlot> CRUCIBLE_SLOTS_INPUT;
    public static final List<PositionnedSlot> CRUCIBLE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> REFINERY_SLOTS;
    public static final List<PositionnedSlot> REFINERY_SLOTS_INPUT;
    public static final List<PositionnedSlot> REFINERY_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> BOTTLER_SLOTS;
    public static final List<PositionnedSlot> BOTTLER_SLOTS_INPUT;
    public static final List<PositionnedSlot> BOTTLER_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> PYROLYZER_SLOTS;
    public static final List<PositionnedSlot> PYROLYZER_SLOTS_INPUT;
    public static final List<PositionnedSlot> PYROLYZER_SLOTS_OUTPUT;

    // Create (No slots, it's a flat recipe creator)
    public static final int CREATE_SLOTS_SIZE = 0;

    /**
     * Transform an array of slots into a list
     *
     * @param slots array of slots
     * @return a list of slots (trimmed to size to avoid useless memory usage)
     */
    private static List<PositionnedSlot> toList(PositionnedSlot... slots)
    {
        ArrayList<PositionnedSlot> arrayList = new ArrayList<>(Arrays.asList(slots));
        arrayList.trimToSize();
        return arrayList;
    }

    /**
     * Transform a list of list into a single list
     * @param list list of list
     * @return a single list (trimmed to size to avoid useless memory usage)
     */
    @SafeVarargs
    private static ArrayList<PositionnedSlot> list(List<PositionnedSlot>... list)
    {
        ArrayList<PositionnedSlot> arrayList = Stream.of(list).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
        arrayList.trimToSize();
        return arrayList;
    }

    static
    {
        // Minecraft Vanilla, homemade
        int minecraftSlots = 0;
        // Crafting table
        CRAFTING_TABLE_SLOTS_INPUT = new ArrayList<>();
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
                CRAFTING_TABLE_SLOTS_INPUT.add(new PositionnedSlot(minecraftSlots++, 30 + y * 18, 17 + x * 18));

        CRAFTING_TABLE_SLOTS = list(
                CRAFTING_TABLE_SLOTS_INPUT,
                CRAFTING_TABLE_SLOTS_OUTPUT = toList(new PositionnedSlot(minecraftSlots++, 124, 35)));
        FURNACE_SLOTS = list(
                FURNACE_SLOTS_INPUT = toList(new PositionnedSlot(minecraftSlots++, 56, 35)),
                FURNACE_SLOTS_OUTPUT = toList(new PositionnedSlot(minecraftSlots++, 116, 35)));
        SMITHING_SLOTS = list(
                SMITHING_SLOTS_INPUT = toList(new PositionnedSlot(minecraftSlots++, 27, 47), new PositionnedSlot(minecraftSlots++, 76, 47)),
                SMITHING_SLOTS_OUTPUT = toList(new PositionnedSlot(minecraftSlots++, 134, 47)));
        STONECUTTING_SLOTS = list(
                STONECUTTING_SLOTS_INPUT = toList(new PositionnedSlot(minecraftSlots++, 39, 33)),
                STONECUTTING_SLOTS_OUTPUT = toList(new PositionnedSlot(minecraftSlots++, 114, 33)));

        MINECRAFT_SLOTS_INPUT = list(
                CRAFTING_TABLE_SLOTS_INPUT,
                FURNACE_SLOTS_INPUT,
                SMITHING_SLOTS_INPUT,
                STONECUTTING_SLOTS_INPUT);

        MINECRAFT_SLOTS_OUTPUT = list(
                CRAFTING_TABLE_SLOTS_OUTPUT,
                FURNACE_SLOTS_OUTPUT,
                SMITHING_SLOTS_OUTPUT,
                STONECUTTING_SLOTS_OUTPUT);

        MINECRAFT_SLOTS = list(
                CRAFTING_TABLE_SLOTS,
                FURNACE_SLOTS,
                SMITHING_SLOTS,
                STONECUTTING_SLOTS);

        MINECRAFT_SLOTS_SIZE = minecraftSlots;


        // Botania
        int botaniaSlots = 0;
        MANA_INFUSION_SLOTS = list(
                MANA_INFUSION_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 80, 61)),
                MANA_INFUSION_SLOTS_OUTPUT = toList(new PositionnedSlot(botaniaSlots++, 116, 37)));
        ELVEN_TRADE_SLOTS = list(
                ELVEN_TRADE_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 26, 19), new PositionnedSlot(botaniaSlots++, 8, 37), new PositionnedSlot(botaniaSlots++, 26, 37), new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 26, 55)),
                ELVEN_TRADE_SLOTS_OUTPUT = toList(new PositionnedSlot(botaniaSlots++, 134, 19), new PositionnedSlot(botaniaSlots++, 116, 37), new PositionnedSlot(botaniaSlots++, 134, 37), new PositionnedSlot(botaniaSlots++, 152, 37), new PositionnedSlot(botaniaSlots++, 134, 55)));
        PURE_DAISY_SLOTS = list(
                PURE_DAISY_SLOTS_INPUT = toList(new DefinedPositionnedSlot(botaniaSlots++, 26, 53, is -> Block.byItem(is.getItem()) != Blocks.AIR)),
                PURE_DAISY_SLOTS_OUTPUT = toList(new DefinedPositionnedSlot(botaniaSlots++, 134, 53, is -> Block.byItem(is.getItem()) != Blocks.AIR)));
        BREWERY_SLOTS = list(
                BREWERY_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 24, 12), new PositionnedSlot(botaniaSlots++, 45, 12), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 57, 33), new PositionnedSlot(botaniaSlots++, 24, 54), new PositionnedSlot(botaniaSlots++, 45, 54)),
                BREWERY_SLOTS_OUTPUT = toList(new DefinedPositionnedSlot(botaniaSlots++, 134, 33, is -> is.getItem() instanceof IBrewItem)));
        PETAL_APOTHECARY_SLOTS = list(
                PETAL_APOTHECARY_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 15, 12), new PositionnedSlot(botaniaSlots++, 35, 12), new PositionnedSlot(botaniaSlots++, 55, 12), new PositionnedSlot(botaniaSlots++, 23, 33), new PositionnedSlot(botaniaSlots++, 43, 33), new PositionnedSlot(botaniaSlots++, 63, 33), new PositionnedSlot(botaniaSlots++, 15, 54), new PositionnedSlot(botaniaSlots++, 35, 54), new PositionnedSlot(botaniaSlots++, 55, 54)),
                PETAL_APOTHECARY_SLOTS_OUTPUT = toList(new PositionnedSlot(botaniaSlots++, 134, 33)));
        RUNIC_ALTAR_SLOTS = list(
                RUNIC_ALTAR_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 8, 8), new PositionnedSlot(botaniaSlots++, 26, 8), new PositionnedSlot(botaniaSlots++, 44, 8), new PositionnedSlot(botaniaSlots++, 8, 26), new PositionnedSlot(botaniaSlots++, 26, 26), new PositionnedSlot(botaniaSlots++, 44, 26), new PositionnedSlot(botaniaSlots++, 62, 26), new PositionnedSlot(botaniaSlots++, 8, 44), new PositionnedSlot(botaniaSlots++, 26, 44), new PositionnedSlot(botaniaSlots++, 44, 44), new PositionnedSlot(botaniaSlots++, 62, 44), new PositionnedSlot(botaniaSlots++, 8, 62), new PositionnedSlot(botaniaSlots++, 26, 62), new PositionnedSlot(botaniaSlots++, 44, 62)),
                RUNIC_ALTAR_SLOTS_OUTPUT = toList(new PositionnedSlot(botaniaSlots++, 134, 33)));
        TERRA_PLATE_SLOTS = list(
                TERRA_PLATE_SLOTS_INPUT = toList(new PositionnedSlot(botaniaSlots++, 34, 11), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 56, 33), new PositionnedSlot(botaniaSlots++, 22, 57), new PositionnedSlot(botaniaSlots++, 47, 57)),
                TERRA_PLATE_SLOTS_OUTPUT = toList(new PositionnedSlot(botaniaSlots++, 134, 33)));

        BOTANIA_SLOTS_INPUT = list(
                MANA_INFUSION_SLOTS_INPUT,
                ELVEN_TRADE_SLOTS_INPUT,
                PURE_DAISY_SLOTS_INPUT,
                BREWERY_SLOTS_INPUT,
                PETAL_APOTHECARY_SLOTS_INPUT,
                RUNIC_ALTAR_SLOTS_INPUT,
                TERRA_PLATE_SLOTS_INPUT);

        BOTANIA_SLOTS_OUTPUT = list(
                MANA_INFUSION_SLOTS_OUTPUT,
                ELVEN_TRADE_SLOTS_OUTPUT,
                PURE_DAISY_SLOTS_OUTPUT,
                BREWERY_SLOTS_OUTPUT,
                PETAL_APOTHECARY_SLOTS_OUTPUT,
                RUNIC_ALTAR_SLOTS_OUTPUT,
                TERRA_PLATE_SLOTS_OUTPUT);

        BOTANIA_SLOTS = list(
                MANA_INFUSION_SLOTS,
                ELVEN_TRADE_SLOTS,
                PURE_DAISY_SLOTS,
                BREWERY_SLOTS,
                PETAL_APOTHECARY_SLOTS,
                RUNIC_ALTAR_SLOTS,
                TERRA_PLATE_SLOTS);

        BOTANIA_SLOTS_SIZE = botaniaSlots;


        // Thermal
        int thermalSlots = 0;
        TREE_EXTRACTOR_SLOTS = list(
                TREE_EXTRACTOR_SLOTS_INPUT = toList(new DefinedPositionnedSlot(thermalSlots++, 22, 98, is -> Block.byItem(is.getItem()) != Blocks.AIR), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> Block.byItem(is.getItem()) != Blocks.AIR)),
                TREE_EXTRACTOR_SLOTS_OUTPUT = toList(new DefinedPositionnedSlot(thermalSlots++, 189, 72, is -> is.getItem() instanceof BucketItem)));
        PULVERIZER_SLOTS = list(
                PULVERIZER_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                PULVERIZER_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111)));
        SAWMILL_SLOTS = list(
                SAWMILL_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                SAWMILL_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111)));
        SMELTER_SLOTS = list(
                SMELTER_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 14, 44), new PositionnedSlot(thermalSlots++, 14, 62), new PositionnedSlot(thermalSlots++, 14, 80), new PositionnedSlot(thermalSlots++, 14, 98), new PositionnedSlot(thermalSlots++, 44, 44), new PositionnedSlot(thermalSlots++, 44, 62), new PositionnedSlot(thermalSlots++, 44, 80), new PositionnedSlot(thermalSlots++, 44, 98), new PositionnedSlot(thermalSlots++, 74, 44), new PositionnedSlot(thermalSlots++, 74, 62), new PositionnedSlot(thermalSlots++, 74, 80), new PositionnedSlot(thermalSlots++, 74, 98)),
                SMELTER_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111)));
        INSOLATOR_SLOTS = list(
                INSOLATOR_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                INSOLATOR_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111)));
        PRESS_SLOTS = list(
                PRESS_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 22, 98), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> is.getItem().getTags().contains(CommonUtils.parse("thermal:crafting/dies")))),
                PRESS_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 41), new PositionnedSlot(thermalSlots++, 189, 103)));
        THERMAL_FURNACE_SLOTS = list(
                THERMAL_FURNACE_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                THERMAL_FURNACE_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 72)));
        CENTRIFUGE_SLOTS = list(
                CENTRIFUGE_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                CENTRIFUGE_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111), new DefinedPositionnedSlot(thermalSlots++, 242, 84, is -> is.getItem() instanceof BucketItem)));
        CHILLER_SLOTS = list(
                CHILLER_SLOTS_INPUT = toList(new DefinedPositionnedSlot(thermalSlots++, 29, 98, is -> is.getItem() instanceof BucketItem), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> is.getItem().getTags().contains(CommonUtils.parse("thermal:crafting/casts")))),
                CHILLER_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 72)));
        CRUCIBLE_SLOTS = list(
                CRUCIBLE_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                CRUCIBLE_SLOTS_OUTPUT = toList(new DefinedPositionnedSlot(thermalSlots++, 189, 72, is -> is.getItem() instanceof BucketItem)));
        REFINERY_SLOTS = list(
                REFINERY_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                REFINERY_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new DefinedPositionnedSlot(thermalSlots++, 189, 72, is -> is.getItem() instanceof BucketItem), new DefinedPositionnedSlot(thermalSlots++, 189, 111, is -> is.getItem() instanceof BucketItem)));
        BOTTLER_SLOTS = list(
                BOTTLER_SLOTS_INPUT = toList(new DefinedPositionnedSlot(thermalSlots++, 29, 98, is -> is.getItem() instanceof BucketItem), new PositionnedSlot(thermalSlots++, 83, 98)),
                BOTTLER_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 72)));
        PYROLYZER_SLOTS = list(
                PYROLYZER_SLOTS_INPUT = toList(new PositionnedSlot(thermalSlots++, 63, 98)),
                PYROLYZER_SLOTS_OUTPUT = toList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111), new DefinedPositionnedSlot(thermalSlots++, 259, 86, is -> is.getItem() instanceof BucketItem)));


        THERMAL_SLOTS_INPUT = list(
                TREE_EXTRACTOR_SLOTS_INPUT,
                PULVERIZER_SLOTS_INPUT,
                SAWMILL_SLOTS_INPUT, 
                SMELTER_SLOTS_INPUT, 
                INSOLATOR_SLOTS_INPUT, 
                PRESS_SLOTS_INPUT, 
                THERMAL_FURNACE_SLOTS_INPUT,
                CENTRIFUGE_SLOTS_INPUT,
                CHILLER_SLOTS_INPUT,
                CRUCIBLE_SLOTS_INPUT,
                REFINERY_SLOTS_INPUT,
                BOTTLER_SLOTS_INPUT,
                PYROLYZER_SLOTS_INPUT);

        THERMAL_SLOTS_OUTPUT = list(
                TREE_EXTRACTOR_SLOTS_OUTPUT,
                PULVERIZER_SLOTS_OUTPUT,
                SAWMILL_SLOTS_OUTPUT,
                SMELTER_SLOTS_OUTPUT,
                INSOLATOR_SLOTS_OUTPUT,
                PRESS_SLOTS_OUTPUT,
                THERMAL_FURNACE_SLOTS_OUTPUT,
                CENTRIFUGE_SLOTS_OUTPUT,
                CHILLER_SLOTS_OUTPUT,
                CRUCIBLE_SLOTS_OUTPUT,
                REFINERY_SLOTS_OUTPUT,
                BOTTLER_SLOTS_OUTPUT,
                PYROLYZER_SLOTS_OUTPUT);

        THERMAL_SLOTS = list(
                TREE_EXTRACTOR_SLOTS,
                PULVERIZER_SLOTS,
                SAWMILL_SLOTS,
                SMELTER_SLOTS,
                INSOLATOR_SLOTS,
                PRESS_SLOTS,
                THERMAL_FURNACE_SLOTS,
                CENTRIFUGE_SLOTS,
                CHILLER_SLOTS,
                CRUCIBLE_SLOTS,
                REFINERY_SLOTS,
                BOTTLER_SLOTS,
                PYROLYZER_SLOTS);

        THERMAL_SLOTS_SIZE = thermalSlots;
    }
}
