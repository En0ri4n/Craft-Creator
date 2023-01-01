package fr.eno.craftcreator.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlotHelper
{
    // Minecraft
    public static final int CRAFTING_TABLE_SLOTS_SIZE = 10;
    public static final int FURNACE_SLOTS_SIZE = 3;
    public static final int SMITHING_TABLE_SLOTS_SIZE = 3;
    public static final int STONECUTTER_SLOTS_SIZE = 2;

    // Botania
    public static final int BOTANIA_SLOTS_SIZE;
    public static final List<PositionnedSlot> BOTANIA_SLOTS;
    public static final List<PositionnedSlot> MANA_INFUSION_SLOTS;
    public static final List<PositionnedSlot> ELVEN_TRADE_SLOTS;
    public static final List<PositionnedSlot> PURE_DAISY_SLOTS;
    public static final List<PositionnedSlot> BREWERY_SLOTS;
    public static final List<PositionnedSlot> PETAL_APOTHECARY_SLOTS;
    public static final List<PositionnedSlot> RUNIC_ALTAR_SLOTS;
    public static final List<PositionnedSlot> TERRA_PLATE_SLOTS;

    public static final int THERMAL_SLOTS_SIZE;
    public static final List<PositionnedSlot> THERMAL_SLOTS;
    public static final List<PositionnedSlot> TREE_EXTRACTOR_SLOTS;
    public static final List<PositionnedSlot> PULVERIZER_SLOTS;
    public static final List<PositionnedSlot> SAWMILL_SLOTS;
    public static final List<PositionnedSlot> SMELTER_SLOTS;


    static
    {
        int botaniaSlots = 0;
        MANA_INFUSION_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 80, 61), new PositionnedSlot(botaniaSlots++, 116, 37));
        ELVEN_TRADE_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 26, 19), new PositionnedSlot(botaniaSlots++, 8, 37), new PositionnedSlot(botaniaSlots++, 26, 37), new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 26, 55), new PositionnedSlot(botaniaSlots++, 134, 19), new PositionnedSlot(botaniaSlots++, 116, 37), new PositionnedSlot(botaniaSlots++, 134, 37), new PositionnedSlot(botaniaSlots++, 152, 37), new PositionnedSlot(botaniaSlots++, 134, 55));
        PURE_DAISY_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 26, 53), new PositionnedSlot(botaniaSlots++, 134, 53));
        BREWERY_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 24, 12), new PositionnedSlot(botaniaSlots++, 45, 12), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 57, 33), new PositionnedSlot(botaniaSlots++, 24, 54), new PositionnedSlot(botaniaSlots++, 45, 54), new PositionnedSlot(botaniaSlots++, 134, 33));
        PETAL_APOTHECARY_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 15, 12), new PositionnedSlot(botaniaSlots++, 35, 12), new PositionnedSlot(botaniaSlots++, 55, 12), new PositionnedSlot(botaniaSlots++, 23, 33), new PositionnedSlot(botaniaSlots++, 43, 33), new PositionnedSlot(botaniaSlots++, 63, 33), new PositionnedSlot(botaniaSlots++, 15, 54), new PositionnedSlot(botaniaSlots++, 35, 54), new PositionnedSlot(botaniaSlots++, 55, 54), new PositionnedSlot(botaniaSlots++, 134, 33));
        RUNIC_ALTAR_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 8, 8), new PositionnedSlot(botaniaSlots++, 26, 8), new PositionnedSlot(botaniaSlots++, 44, 8), new PositionnedSlot(botaniaSlots++, 8, 26), new PositionnedSlot(botaniaSlots++, 26, 26), new PositionnedSlot(botaniaSlots++, 44, 26), new PositionnedSlot(botaniaSlots++, 62, 26), new PositionnedSlot(botaniaSlots++, 8, 44), new PositionnedSlot(botaniaSlots++, 26, 44), new PositionnedSlot(botaniaSlots++, 44, 44), new PositionnedSlot(botaniaSlots++, 62, 44), new PositionnedSlot(botaniaSlots++, 8, 62), new PositionnedSlot(botaniaSlots++, 26, 62), new PositionnedSlot(botaniaSlots++, 44, 62), new PositionnedSlot(botaniaSlots++, 134, 33));
        TERRA_PLATE_SLOTS = Arrays.asList(new PositionnedSlot(botaniaSlots++, 34, 11), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 56, 33), new PositionnedSlot(botaniaSlots++, 22, 57), new PositionnedSlot(botaniaSlots++, 47, 57), new PositionnedSlot(botaniaSlots++, 134, 33));
        BOTANIA_SLOTS = Stream.of(MANA_INFUSION_SLOTS, ELVEN_TRADE_SLOTS, PURE_DAISY_SLOTS, BREWERY_SLOTS, PETAL_APOTHECARY_SLOTS, RUNIC_ALTAR_SLOTS, TERRA_PLATE_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        BOTANIA_SLOTS_SIZE = botaniaSlots;
        
        int thermalSlots = 0;
        TREE_EXTRACTOR_SLOTS = Arrays.asList(new PositionnedSlot(thermalSlots++, 22, 98), new PositionnedSlot(thermalSlots++, 83, 98), new PositionnedSlot(thermalSlots++, 189, 72));
        PULVERIZER_SLOTS = Arrays.asList(new PositionnedSlot(thermalSlots++, 63, 98), new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SAWMILL_SLOTS = Arrays.asList(new PositionnedSlot(thermalSlots++, 63, 99), new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SMELTER_SLOTS = Arrays.asList(new PositionnedSlot(thermalSlots++, 14, 44), new PositionnedSlot(thermalSlots++, 14, 62), new PositionnedSlot(thermalSlots++, 14, 80), new PositionnedSlot(thermalSlots++, 14, 98), new PositionnedSlot(thermalSlots++, 44, 44), new PositionnedSlot(thermalSlots++, 44, 62), new PositionnedSlot(thermalSlots++, 44, 80), new PositionnedSlot(thermalSlots++, 44, 98), new PositionnedSlot(thermalSlots++, 74, 44), new PositionnedSlot(thermalSlots++, 74, 62), new PositionnedSlot(thermalSlots++, 74, 80), new PositionnedSlot(thermalSlots++, 74, 98), new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));

        THERMAL_SLOTS = Stream.of(TREE_EXTRACTOR_SLOTS, PULVERIZER_SLOTS, SAWMILL_SLOTS, SMELTER_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        THERMAL_SLOTS_SIZE = thermalSlots;
    }
}
