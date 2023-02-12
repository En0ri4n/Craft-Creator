package fr.eno.craftcreator.utils;

import fr.eno.craftcreator.api.ClientUtils;
import fr.eno.craftcreator.container.slot.utils.DefinedPositionnedSlot;
import fr.eno.craftcreator.container.slot.utils.PositionnedSlot;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BucketItem;
import vazkii.botania.api.brew.IBrewItem;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SlotHelper
{
    // Minecraft
    public static final int MINECRAFT_SLOTS_SIZE;
    public static final List<PositionnedSlot> MINECRAFT_SLOTS;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS_INPUT;
    public static final List<PositionnedSlot> CRAFTING_TABLE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> FURNACE_SLOTS;
    public static final List<PositionnedSlot> SMITHING_TABLE_SLOTS;
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
    public static final List<PositionnedSlot> PRESS_SLOTS;
    public static final List<PositionnedSlot> PRESS_SLOTS_INPUT;
    public static final List<PositionnedSlot> PRESS_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> FURNACE_THERMAL_SLOTS;
    public static final List<PositionnedSlot> FURNACE_THERMAL_SLOTS_INPUT;
    public static final List<PositionnedSlot> FURNACE_THERMAL_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS_INPUT;
    public static final List<PositionnedSlot> CENTRIFUGE_SLOTS_OUTPUT;
    public static final List<PositionnedSlot> CHILLER_SLOTS;
    public static final List<PositionnedSlot> CHILLER_SLOTS_INPUT;
    public static final List<PositionnedSlot> CHILLER_SLOTS_OUTPUT;

    static
    {
        // Vanilla, homemade
        int minecraftSlots = 0;
        // Crafting table
        CRAFTING_TABLE_SLOTS_INPUT = new ArrayList<>();
        for(int x = 0; x < 3; ++x)
            for(int y = 0; y < 3; ++y)
                CRAFTING_TABLE_SLOTS_INPUT.add(new PositionnedSlot(minecraftSlots++, 30 + y * 18, 17 + x * 18));
        CRAFTING_TABLE_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(minecraftSlots++, 124, 35));
        CRAFTING_TABLE_SLOTS = Stream.of(CRAFTING_TABLE_SLOTS_INPUT, CRAFTING_TABLE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Furnace
        FURNACE_SLOTS = Arrays.asList(new PositionnedSlot(minecraftSlots++, 56, 17), new PositionnedSlot(minecraftSlots++, 116, 35), new PositionnedSlot(minecraftSlots++, 56, 53));
        // Smithing table
        SMITHING_TABLE_SLOTS = Arrays.asList(new PositionnedSlot(minecraftSlots++, 27, 47), new PositionnedSlot(minecraftSlots++, 76, 47), new PositionnedSlot(minecraftSlots++, 134, 47));
        // Stonecutter
        STONECUTTER_SLOTS = Arrays.asList(new PositionnedSlot(minecraftSlots++, 39, 33), new PositionnedSlot(minecraftSlots++, 114, 33));

        MINECRAFT_SLOTS = Stream.of(CRAFTING_TABLE_SLOTS, FURNACE_SLOTS, SMITHING_TABLE_SLOTS, STONECUTTER_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        MINECRAFT_SLOTS_SIZE = minecraftSlots;


        // Botania
        int botaniaSlots = 0;
        // Mana pool
        MANA_INFUSION_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 80, 61));
        MANA_INFUSION_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(botaniaSlots++, 116, 37));
        MANA_INFUSION_SLOTS = Stream.of(MANA_INFUSION_SLOTS_INPUT, MANA_INFUSION_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Elven trade
        ELVEN_TRADE_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 26, 19), new PositionnedSlot(botaniaSlots++, 8, 37), new PositionnedSlot(botaniaSlots++, 26, 37), new PositionnedSlot(botaniaSlots++, 44, 37), new PositionnedSlot(botaniaSlots++, 26, 55));
        ELVEN_TRADE_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 134, 19), new PositionnedSlot(botaniaSlots++, 116, 37), new PositionnedSlot(botaniaSlots++, 134, 37), new PositionnedSlot(botaniaSlots++, 152, 37), new PositionnedSlot(botaniaSlots++, 134, 55));
        ELVEN_TRADE_SLOTS = Stream.of(ELVEN_TRADE_SLOTS_INPUT, ELVEN_TRADE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Pure daisy
        PURE_DAISY_SLOTS_INPUT = Collections.singletonList(new DefinedPositionnedSlot(botaniaSlots++, 26, 53, is -> Block.byItem(is.getItem()) != Blocks.AIR));
        PURE_DAISY_SLOTS_OUTPUT = Collections.singletonList(new DefinedPositionnedSlot(botaniaSlots++, 134, 53, is -> Block.byItem(is.getItem()) != Blocks.AIR));
        PURE_DAISY_SLOTS = Stream.of(PURE_DAISY_SLOTS_INPUT, PURE_DAISY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Brewery
        BREWERY_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 24, 12), new PositionnedSlot(botaniaSlots++, 45, 12), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 57, 33), new PositionnedSlot(botaniaSlots++, 24, 54), new PositionnedSlot(botaniaSlots++, 45, 54));
        BREWERY_SLOTS_OUTPUT = Collections.singletonList(new DefinedPositionnedSlot(botaniaSlots++, 134, 33, is -> is.getItem() instanceof IBrewItem));
        BREWERY_SLOTS = Stream.of(BREWERY_SLOTS_INPUT, BREWERY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Petal apothecary
        PETAL_APOTHECARY_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 15, 12), new PositionnedSlot(botaniaSlots++, 35, 12), new PositionnedSlot(botaniaSlots++, 55, 12), new PositionnedSlot(botaniaSlots++, 23, 33), new PositionnedSlot(botaniaSlots++, 43, 33), new PositionnedSlot(botaniaSlots++, 63, 33), new PositionnedSlot(botaniaSlots++, 15, 54), new PositionnedSlot(botaniaSlots++, 35, 54), new PositionnedSlot(botaniaSlots++, 55, 54));
        PETAL_APOTHECARY_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(botaniaSlots++, 134, 33));
        PETAL_APOTHECARY_SLOTS = Stream.of(PETAL_APOTHECARY_SLOTS_INPUT, PETAL_APOTHECARY_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Runic altar
        RUNIC_ALTAR_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 8, 8), new PositionnedSlot(botaniaSlots++, 26, 8), new PositionnedSlot(botaniaSlots++, 44, 8), new PositionnedSlot(botaniaSlots++, 8, 26), new PositionnedSlot(botaniaSlots++, 26, 26), new PositionnedSlot(botaniaSlots++, 44, 26), new PositionnedSlot(botaniaSlots++, 62, 26), new PositionnedSlot(botaniaSlots++, 8, 44), new PositionnedSlot(botaniaSlots++, 26, 44), new PositionnedSlot(botaniaSlots++, 44, 44), new PositionnedSlot(botaniaSlots++, 62, 44), new PositionnedSlot(botaniaSlots++, 8, 62), new PositionnedSlot(botaniaSlots++, 26, 62), new PositionnedSlot(botaniaSlots++, 44, 62));
        RUNIC_ALTAR_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(botaniaSlots++, 134, 33));
        RUNIC_ALTAR_SLOTS = Stream.of(RUNIC_ALTAR_SLOTS_INPUT, RUNIC_ALTAR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Terra plate
        TERRA_PLATE_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(botaniaSlots++, 34, 11), new PositionnedSlot(botaniaSlots++, 12, 33), new PositionnedSlot(botaniaSlots++, 56, 33), new PositionnedSlot(botaniaSlots++, 22, 57), new PositionnedSlot(botaniaSlots++, 47, 57));
        TERRA_PLATE_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(botaniaSlots++, 134, 33));
        TERRA_PLATE_SLOTS = Stream.of(TERRA_PLATE_SLOTS_INPUT, TERRA_PLATE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());

        BOTANIA_SLOTS_OUTPUT = Stream.of(MANA_INFUSION_SLOTS_OUTPUT, ELVEN_TRADE_SLOTS_OUTPUT, PURE_DAISY_SLOTS_OUTPUT, BREWERY_SLOTS_OUTPUT, PETAL_APOTHECARY_SLOTS_OUTPUT, RUNIC_ALTAR_SLOTS_OUTPUT, TERRA_PLATE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        BOTANIA_SLOTS = Stream.of(MANA_INFUSION_SLOTS, ELVEN_TRADE_SLOTS, PURE_DAISY_SLOTS, BREWERY_SLOTS, PETAL_APOTHECARY_SLOTS, RUNIC_ALTAR_SLOTS, TERRA_PLATE_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        BOTANIA_SLOTS_SIZE = botaniaSlots;


        // Thermal
        int thermalSlots = 0;
        // Tree Extractor
        TREE_EXTRACTOR_SLOTS_INPUT = Arrays.asList(new DefinedPositionnedSlot(thermalSlots++, 22, 98, is -> Block.byItem(is.getItem()) != Blocks.AIR), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> Block.byItem(is.getItem()) != Blocks.AIR));
        TREE_EXTRACTOR_SLOTS_OUTPUT = Collections.singletonList(new DefinedPositionnedSlot(thermalSlots++, 189, 72, is -> is.getItem() instanceof BucketItem));
        TREE_EXTRACTOR_SLOTS = Stream.of(TREE_EXTRACTOR_SLOTS_INPUT, TREE_EXTRACTOR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Pulverizer
        PULVERIZER_SLOTS_INPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 63, 98));
        PULVERIZER_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        PULVERIZER_SLOTS = Stream.of(PULVERIZER_SLOTS_INPUT, PULVERIZER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Sawmill
        SAWMILL_SLOTS_INPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 63, 98));
        SAWMILL_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SAWMILL_SLOTS = Stream.of(SAWMILL_SLOTS_INPUT, SAWMILL_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Induction Smelter
        SMELTER_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 14, 44), new PositionnedSlot(thermalSlots++, 14, 62), new PositionnedSlot(thermalSlots++, 14, 80), new PositionnedSlot(thermalSlots++, 14, 98), new PositionnedSlot(thermalSlots++, 44, 44), new PositionnedSlot(thermalSlots++, 44, 62), new PositionnedSlot(thermalSlots++, 44, 80), new PositionnedSlot(thermalSlots++, 44, 98), new PositionnedSlot(thermalSlots++, 74, 44), new PositionnedSlot(thermalSlots++, 74, 62), new PositionnedSlot(thermalSlots++, 74, 80), new PositionnedSlot(thermalSlots++, 74, 98));
        SMELTER_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        SMELTER_SLOTS = Stream.of(SMELTER_SLOTS_INPUT, SMELTER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Insolator
        INSOLATOR_SLOTS_INPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 63, 98));
        INSOLATOR_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111));
        INSOLATOR_SLOTS = Stream.of(INSOLATOR_SLOTS_INPUT, INSOLATOR_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Press
        PRESS_SLOTS_INPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 22, 98), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> is.getItem().getTags().contains(ClientUtils.parse("thermal:crafting/dies"))));
        PRESS_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 41), new PositionnedSlot(thermalSlots++, 189, 103));
        PRESS_SLOTS = Stream.of(PRESS_SLOTS_INPUT, PRESS_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Thermal Furnace
        FURNACE_THERMAL_SLOTS_INPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 63, 98));
        FURNACE_THERMAL_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 189, 72));
        FURNACE_THERMAL_SLOTS = Stream.of(FURNACE_THERMAL_SLOTS_INPUT, FURNACE_THERMAL_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Centrifuge
        CENTRIFUGE_SLOTS_INPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 63, 98));
        CENTRIFUGE_SLOTS_OUTPUT = Arrays.asList(new PositionnedSlot(thermalSlots++, 189, 33), new PositionnedSlot(thermalSlots++, 189, 59), new PositionnedSlot(thermalSlots++, 189, 85), new PositionnedSlot(thermalSlots++, 189, 111), new DefinedPositionnedSlot(thermalSlots++, 242, 84, is -> is.getItem() instanceof BucketItem));
        CENTRIFUGE_SLOTS = Stream.of(CENTRIFUGE_SLOTS_INPUT, CENTRIFUGE_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        // Chiller
        CHILLER_SLOTS_INPUT = Arrays.asList(new DefinedPositionnedSlot(thermalSlots++, 29, 98, is -> is.getItem() instanceof BucketItem), new DefinedPositionnedSlot(thermalSlots++, 83, 98, is -> is.getItem().getTags().contains(ClientUtils.parse("thermal:crafting/casts"))));
        CHILLER_SLOTS_OUTPUT = Collections.singletonList(new PositionnedSlot(thermalSlots++, 189, 72));
        CHILLER_SLOTS = Stream.of(CHILLER_SLOTS_INPUT, CHILLER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());

        THERMAL_SLOTS_OUTPUT = Stream.of(TREE_EXTRACTOR_SLOTS_OUTPUT, PULVERIZER_SLOTS_OUTPUT, SAWMILL_SLOTS_OUTPUT, SMELTER_SLOTS_OUTPUT, INSOLATOR_SLOTS_OUTPUT, PRESS_SLOTS_OUTPUT, FURNACE_THERMAL_SLOTS_OUTPUT, CENTRIFUGE_SLOTS_OUTPUT, CHILLER_SLOTS_OUTPUT).flatMap(Collection::stream).collect(Collectors.toList());
        THERMAL_SLOTS = Stream.of(TREE_EXTRACTOR_SLOTS, PULVERIZER_SLOTS, SAWMILL_SLOTS, SMELTER_SLOTS, INSOLATOR_SLOTS, PRESS_SLOTS, FURNACE_THERMAL_SLOTS, CENTRIFUGE_SLOTS, CHILLER_SLOTS).flatMap(Collection::stream).collect(Collectors.toList());
        THERMAL_SLOTS_SIZE = thermalSlots;
    }
}
