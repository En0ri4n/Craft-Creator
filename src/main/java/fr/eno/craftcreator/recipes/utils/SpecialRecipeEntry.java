package fr.eno.craftcreator.recipes.utils;

import com.google.gson.JsonObject;
import fr.eno.craftcreator.utils.CommonUtils;
import fr.eno.craftcreator.utils.JsonSerializable;
import net.minecraft.util.ResourceLocation;

public class SpecialRecipeEntry implements JsonSerializable
{
    private ResourceLocation registryName;
    private int count;
    private EntryType type;
    private double chance;

    public SpecialRecipeEntry()
    {
        this.registryName = CommonUtils.parse("minecraft:air");
        this.count = 1;
        this.type = EntryType.ITEM;
        this.chance = 1D;
    }

    public ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public void setRegistryName(ResourceLocation registryName)
    {
        this.registryName = registryName;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public boolean isTag()
    {
        return type.isTag();
    }

    public boolean isItem()
    {
        return type.isItem();
    }

    public boolean isFluid()
    {
        return type.isFluid();
    }

    public EntryType getType()
    {
        return type;
    }

    public void setType(EntryType type)
    {
        this.type = type;
    }

    public double getChance()
    {
        return chance;
    }

    public void setChance(double chance)
    {
        this.chance = chance;
    }

    public boolean isEmpty()
    {
        return this.registryName.equals(CommonUtils.parse("minecraft:air"));
    }

    @Override
    public JsonObject serialize()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("registry_name", registryName.toString());
        jsonObject.addProperty("count", count);
        jsonObject.addProperty("type", type.ordinal());
        jsonObject.addProperty("chance", chance);
        return jsonObject;
    }

    public static SpecialRecipeEntry deserialize(JsonObject json)
    {
        SpecialRecipeEntry entry = new SpecialRecipeEntry();
        entry.setRegistryName(CommonUtils.parse(json.get("registry_name").getAsString()));
        entry.setCount(json.get("count").getAsInt());
        entry.setType(EntryType.values()[json.get("type").getAsInt()]);
        entry.setChance(json.get("chance").getAsDouble());
        return entry;
    }
}
