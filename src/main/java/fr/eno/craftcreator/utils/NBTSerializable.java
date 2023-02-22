package fr.eno.craftcreator.utils;

import net.minecraft.nbt.CompoundTag;

public interface NBTSerializable<T>
{
    CompoundTag serialize();

    T deserialize(CompoundTag compound);
}
