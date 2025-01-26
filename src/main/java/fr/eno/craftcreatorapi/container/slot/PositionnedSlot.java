package fr.eno.craftcreatorapi.container.slot;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class PositionnedSlot
{
    public static final PositionnedSlot EMPTY = new PositionnedSlot(0, 0, 0);

    private final int index;
    private final int xPos;
    private final int yPos;

    public static boolean isValidSlot(List<PositionnedSlot> slots, int slotIndex, int index)
    {
        return slots.get(0).getIndex() + slotIndex == index;
    }

    public static boolean contains(List<PositionnedSlot> slots, int index)
    {
        return slots.stream().anyMatch(positionnedSlot -> positionnedSlot.getIndex() == index);
    }

    public static <T extends CCSlot> List<T> getSlotsFor(List<PositionnedSlot> positionnedSlots, List<T> slots)
    {
        List<T> finalSlots = new ArrayList<>();

        for(T slot : slots)
        {
            Optional<PositionnedSlot> optionalPositionnedSlot = positionnedSlots.stream().filter(ps -> ps.getIndex() == slot.getSlotIndex()).findFirst();

            if(optionalPositionnedSlot.isPresent())
                finalSlots.add(slot);
        }

        return finalSlots;
    }
}
