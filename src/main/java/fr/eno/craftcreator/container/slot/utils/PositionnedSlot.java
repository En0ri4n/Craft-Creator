package fr.eno.craftcreator.container.slot.utils;

import net.minecraft.inventory.container.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionnedSlot
{
    public static final PositionnedSlot EMPTY = new PositionnedSlot(0, -1, -1);

    private final int index;
    private final int xPos;
    private final int yPos;

    public PositionnedSlot(int index, int xPos, int yPos)
    {
        this.index = index;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getIndex()
    {
        return index;
    }

    public int getxPos()
    {
        return xPos;
    }

    public int getyPos()
    {
        return yPos;
    }

    public static boolean isValidSlot(List<PositionnedSlot> slots, int slotIndex, int index)
    {
        return slots.get(0).getIndex() + slotIndex == index;
    }

    public static boolean contains(List<PositionnedSlot> slots, int index)
    {
        return slots.stream().anyMatch(positionnedSlot -> positionnedSlot.getIndex() == index);
    }

    public static List<Slot> getSlotsFor(List<PositionnedSlot> positionnedSlots, List<Slot> slots)
    {
        List<Slot> finalSlots = new ArrayList<>();

        for(Slot slot : slots)
        {
            Optional<PositionnedSlot> optionalPositionnedSlot = positionnedSlots.stream().filter(ps -> ps.getIndex() == slot.getSlotIndex()).findFirst();

            if(optionalPositionnedSlot.isPresent()) finalSlots.add(slot);
        }

        return finalSlots;
    }
}
