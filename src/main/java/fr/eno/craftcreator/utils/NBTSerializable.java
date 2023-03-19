package fr.eno.craftcreator.utils;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;

public interface NBTSerializable
{
    JsonObject serialize();
}
