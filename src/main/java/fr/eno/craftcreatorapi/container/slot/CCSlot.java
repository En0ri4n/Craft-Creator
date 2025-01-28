package fr.eno.craftcreatorapi.container.slot;

public interface CCSlot<T> {
    int getSlotIndex();
    T getStack();
}
