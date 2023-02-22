package fr.eno.craftcreator.utils;

import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializable<T>
{
    CompoundNBT serialize();

    T deserialize(CompoundNBT compound);
}
