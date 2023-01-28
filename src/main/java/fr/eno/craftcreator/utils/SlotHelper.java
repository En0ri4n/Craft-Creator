package fr.eno.craftcreator.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlotHelper
{
    // Minecraft
    public static final int CRAFTING_TABLE_SLOTS_SIZE;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS;
    public static final int FURNACE_SLOTS_SIZE;
    public static final List<PositionnedSlot> FURNACE_SLOTS;
    public static final int SMITHING_TABLE_SLOTS_SIZE;
    public static final List<PositionnedSlot> SMITHING_TABLE_SLOTS;
    public static final int STONECUTTER_SLOTS_SIZE;
    public static final List<PositionnedSlot> STONECUTTER_SLOTS;

    // Botania
    public static final int BOTANIA_SLOTS_SIZE;
    public static final List<PositionnedSlot> BOTANIA_SLOTS;
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
    public static final List<PositionnedSlot> BREWERY_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> BREWERY_SLOTS_INPUT;
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

    static
    {
        int craftingTableSlots = 0;
        CRAFTING_TABLE_SLOTS = new ArrayList<>();
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
                CRAFTING_TABLE_SLOTS.add(new PositionnedSlot(craftingTableSlots++, 30 + y * 18, 17 + x * 18));
        CRAFTING_TABLE_SLOTS.add(new PositionnedSlot(craftingTableSlots++, 124, 35));
        CRAFTING_TABLE_SLOTS_SIZE = craftingTableSlots;

        int furnaceSlots = 0;
        FURNACE_SLOTS = new ArrayList<>();
        FURNACE_SLOTS.add(new PositionnedSlot(furnaceSlots++, 56, 17));
        FURNACE_SLOTS.add(new PositionnedSlot(furnaceSlots++, 116, 35));
        FURNACE_SLOTS.add(new PositionnedSlot(furnaceSlots++, 56, 53));
        FURNACE_SLOTS_SIZE = furnaceSlots;

        int smithingTableSlots = 0;
        SMITHING_TABLE_SLOTS = Arrays.asList(
                new PositionnedSlot(smithingTableSlots++, 27, 47),
                new PositionnedSlot(smithingTableSlots++, 76, 47),
                new PositionnedSlot(smithingTableSlots++, 134, 47)
        );
        SMITHING_TABLE_SLOTS_SIZE = smithingTableSlots;

        int stonecutterSlots = 0;
        STONECUTTER_SLOTS = Arrays.asList(
                new PositionnedSlot(stonecutterSlots++, 39, 33),
                new PositionnedSlot(stonecutterSlots++, 114, 33)
        );
        STONECUTTER_SLOTS_SIZE = stonecutterSlots;


        int botaniaSlots = 0;
        MANA_INFUSION_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 80, 61));
        MANA_INFUSION_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 116, 37));
        MANA_INFUSION_SLOTS = Stream.of(MANA_INFUSION_SLOTS_INPUT, MANA_INFUSION_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        ELVEN_TRADE_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 26, 19), new PositionnedSlot(botaniaSlots++, 8, 37), new PositionnedSlot(botaniaSlots++, 26, 37), new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 26, 55));
        ELVEN_TRADE_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 134, 19), new PositionnedSlot(botaniaSlots++, 116, 37), new PositionnedSlot(botaniaSlots++, 134, 37), new PositionnedSlot(botaniaSlots++, 152, 37), new PositionnedSlot(botaniaSlots++, 134, 55));
        ELVEN_TRADE_SLOTS = Stream.of(ELVEN_TRADE_SLOTS_INPUT, ELVEN_TRADE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        PURE_DAISY_SLOTS_INPUT = List.of(new PositionnedSlot(botaniaSlots++, 26, 53));
        PURE_DAISY_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 134, 53));
        PURE_DAISY_SLOTS = Stream.of(PURE_DAISY_SLOTS_INPUT, PURE_DAISY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        BREWERY_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 24, 12), new PositionnedSlot(botaniaSlots++, 45, 12), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 57, 33), new PositionnedSlot(botaniaSlots++, 24, 54), new PositionnedSlot(botaniaSlots++, 45, 54));
        BREWERY_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 134, 33));
        BREWERY_SLOTS = Stream.of(BREWERY_SLOTS_INPUT, BREWERY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        PETAL_APOTHECARY_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 15, 12), new PositionnedSlot(botaniaSlots++, 35, 12), new PositionnedSlot(botaniaSlots++, 55, 12), new PositionnedSlot(botaniaSlots++, 23, 33), new PositionnedSlot(botaniaSlots++, 43, 33), new PositionnedSlot(botaniaSlots++, 63, 33), new PositionnedSlot(botaniaSlots++, 15, 54), new PositionnedSlot(botaniaSlots++, 35, 54), new PositionnedSlot(botaniaSlots++, 55, 54));
        PETAL_APOTHECARY_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 134, 33));
        PETAL_APOTHECARY_SLOTS = Stream.of(PETAL_APOTHECARY_SLOTS_INPUT, PETAL_APOTHECARY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        RUNIC_ALTAR_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 8, 8), new PositionnedSlot(botaniaSlots++, 26, 8), new PositionnedSlot(botaniaSlots++, 44, 8), new PositionnedSlot(botaniaSlots++, 8, 26), new PositionnedSlot(botaniaSlots++, 26, 26), new PositionnedSlot(botaniaSlots++, 44, 26), new PositionnedSlot(botaniaSlots++, 62, 26), new PositionnedSlot(botaniaSlots++, 8, 44), new PositionnedSlot(botaniaSlots++, 26, 44), new PositionnedSlot(botaniaSlots++, 44, 44), new PositionnedSlot(botaniaSlots++, 62, 44), new PositionnedSlot(botaniaSlots++, 8, 62), new PositionnedSlot(botaniaSlots++, 26, 62), new PositionnedSlot(botaniaSlots++, 44, 62));
        RUNIC_ALTAR_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 134, 33));
        RUNIC_ALTAR_SLOTS = Stream.of(RUNIC_ALTAR_SLOTS_INPUT, RUNIC_ALTAR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        TERRA_PLATE_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 34, 11), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 56, 33), new PositionnedSlot(botaniaSlots++, 22, 57), new PositionnedSlot(botaniaSlots++, 47, 57));
        TERRA_PLATE_SLOTS_OUTPUT = List.of(new PositionnedSlot(botaniaSlots++, 134, 33));
        TERRA_PLATE_SLOTS = Stream.of(TERRA_PLATE_SLOTS_INPUT, TERRA_PLATE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());

        BOTANIA_SLOTS_OUTPUT = Stream.of(MANA_INFUSION_SLOTS_OUTPUT, ELVEN_TRADE_SLOTS_OUTPUT, PURE_DAISY_SLOTS_OUTPUT, BREWERY_SLOTS_OUTPUT, PETAL_APOTHECARY_SLOTS_OUTPUT, RUNIC_ALTAR_SLOTS_OUTPUT, TERRA_PLATE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        BOTANIA_SLOTS = Stream.of(MANA_INFUSION_SLOTS, ELVEN_TRADE_SLOTS, PURE_DAISY_SLOTS, BREWERY_SLOTS, PETAL_APOTHECARY_SLOTS, RUNIC_ALTAR_SLOTS, TERRA_PLATE_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        BOTANIA_SLOTS_SIZE = botaniaSlots;


        int thermalSlots = 0;
        TREE_EXTRACTOR_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 22, 98), new PositionnedSlot(thermalSlots++, 83, 98));
        TREE_EXTRACTOR_SLOTS_OUTPUT = List.of(new PositionnedSlot(thermalSlots++, 189, 72));
        TREE_EXTRACTOR_SLOTS = Stream.of(TREE_EXTRACTOR_SLOTS_INPUT, TREE_EXTRACTOR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        PULVERIZER_SLOTS_INPUT = List.of(new PositionnedSlot(thermalSlots++, 63, 98));
        PULVERIZER_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        PULVERIZER_SLOTS = Stream.of(PULVERIZER_SLOTS_INPUT, PULVERIZER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        SAWMILL_SLOTS_INPUT = List.of(new PositionnedSlot(thermalSlots++, 63, 98));
        SAWMILL_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SAWMILL_SLOTS = Stream.of(SAWMILL_SLOTS_INPUT, SAWMILL_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        SMELTER_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 14, 44), new PositionnedSlot(thermalSlots++, 14, 62), new PositionnedSlot(thermalSlots++, 14, 80), new PositionnedSlot(thermalSlots++, 14, 98), new PositionnedSlot(thermalSlots++, 44, 44), new PositionnedSlot(thermalSlots++, 44, 62), new PositionnedSlot(thermalSlots++, 44, 80), new PositionnedSlot(thermalSlots++, 44, 98), new PositionnedSlot(thermalSlots++, 74, 44), new PositionnedSlot(thermalSlots++, 74, 62), new PositionnedSlot(thermalSlots++, 74, 80), new PositionnedSlot(thermalSlots++, 74, 98));
        SMELTER_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SMELTER_SLOTS = Stream.of(SMELTER_SLOTS_INPUT, SMELTER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        INSOLATOR_SLOTS_INPUT = List.of(new PositionnedSlot(thermalSlots++, 63, 98));
        INSOLATOR_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        INSOLATOR_SLOTS = Stream.of(INSOLATOR_SLOTS_INPUT, INSOLATOR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());

        THERMAL_SLOTS_OUTPUT = Stream.of(TREE_EXTRACTOR_SLOTS_OUTPUT, PULVERIZER_SLOTS_OUTPUT, SAWMILL_SLOTS_OUTPUT, SMELTER_SLOTS_OUTPUT, INSOLATOR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        THERMAL_SLOTS = Stream.of(TREE_EXTRACTOR_SLOTS, PULVERIZER_SLOTS, SAWMILL_SLOTS, SMELTER_SLOTS, INSOLATOR_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        THERMAL_SLOTS_SIZE = thermalSlots;
    }
}
