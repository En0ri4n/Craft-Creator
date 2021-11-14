package fr.eno.craftcreator.utils;

import java.util.*;

public class SlotHelper
{
    // Botania
    public static final int BOTANIA_SLOTS_SIZE;
    public static final List<PositionnedSlot> MANA_INFUSION_SLOTS;
    public static final List<PositionnedSlot> ELVEN_TRADE_SLOTS;
    public static final List<PositionnedSlot> PURE_DAISY_SLOTS;
    public static final List<PositionnedSlot> BREWERY_SLOTS;
    public static final List<PositionnedSlot> PETAL_APOTHECARY_SLOTS;
    public static final List<PositionnedSlot> RUNIC_ALTAR_SLOTS;
    public static final List<PositionnedSlot> TERRA_PLATE_SLOTS;

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
        BOTANIA_SLOTS_SIZE = botaniaSlots;
    }
}
