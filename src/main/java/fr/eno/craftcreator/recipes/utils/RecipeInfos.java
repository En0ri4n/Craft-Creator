package fr.eno.craftcreator.recipes.utils;

import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.utils.NBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeInfos implements NBTSerializable
{
    private final List<RecipeParameter> parameters;

    private RecipeInfos()
    {
        this.parameters = new ArrayList<>();
    }

    public static RecipeInfos create()
    {
        return new RecipeInfos();
    }

    public void addParameter(RecipeParameter parameter)
    {
        this.parameters.add(parameter);
    }

    public List<RecipeParameter> getParameters()
    {
        return this.parameters;
    }

    public RecipeParameter getRecipeParameter(String name)
    {
        return this.parameters.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(RecipeParameter.EMPTY);
    }

    public Number getValue(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterNumber ? ((RecipeParameterNumber) parameter).getNumberValue() : -1;
    }

    public boolean getBoolean(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterBoolean && ((RecipeParameterBoolean) parameter).getBoolean();
    }

    public Map<Integer, ResourceLocation> getMap(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterMap ? ((RecipeParameterMap) parameter).getMap() : new HashMap<>();
    }

    public List<Integer> getList(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterIntList ? ((RecipeParameterIntList) parameter).getList() : new ArrayList<>();
    }

    public boolean contains(String name)
    {
        return this.getRecipeParameter(name) != null;
    }

    @Override
    public CompoundTag serialize()
    {
        CompoundTag compound = new CompoundTag();
        for(RecipeParameter parameter : this.parameters)
        {
            CompoundTag parameterCompound = new CompoundTag();
            parameterCompound.putString("type", parameter.getType().name());

            switch(parameter.getType())
            {
                case NUMBER:
                    RecipeParameterNumber number = (RecipeParameterNumber) parameter;
                    parameterCompound.putBoolean("is_double", number.isDouble());
                    parameterCompound.putDouble("value", number.isDouble() ? number.getNumberValue().doubleValue() : number.getNumberValue().intValue());
                    break;
                case BOOLEAN:
                    RecipeParameterBoolean bool = (RecipeParameterBoolean) parameter;
                    parameterCompound.putBoolean("value", bool.getBoolean());
                    break;
                case INT_LIST:
                    RecipeParameterIntList intList = (RecipeParameterIntList) parameter;
                    IntArrayTag arrayNBT = new IntArrayTag(intList.getList());
                    parameterCompound.put("value", arrayNBT);
                    break;
                case MAP:
                    RecipeParameterMap map = (RecipeParameterMap) parameter;
                    CompoundTag nbt = new CompoundTag();
                    for(Map.Entry<Integer, ResourceLocation> entry : map.getMap().entrySet())
                        nbt.putInt(entry.getValue().toString(), entry.getKey());
                    parameterCompound.put("value", nbt);
                    break;
            }

            compound.put(parameter.getName(), parameterCompound);
        }
        return compound;
    }

    public static RecipeInfos deserialize(CompoundTag compound)
    {
        RecipeInfos recipeInfos = RecipeInfos.create();

        for(String keys : compound.getAllKeys())
        {
            CompoundTag parameterCompound = compound.getCompound(keys);
            RecipeParameterType type = RecipeParameterType.valueOf(parameterCompound.getString("type"));

            switch(type)
            {
                case NUMBER:
                    boolean isDouble = parameterCompound.getBoolean("is_double");
                    Number value = isDouble ? parameterCompound.getDouble("value") : parameterCompound.getInt("value");
                    recipeInfos.addParameter(new RecipeParameterNumber(keys, value, isDouble));
                    break;
                case BOOLEAN:
                    recipeInfos.addParameter(new RecipeParameterBoolean(keys, parameterCompound.getBoolean("value")));
                    break;
                case INT_LIST:
                    IntArrayTag arrayNBT = (IntArrayTag) parameterCompound.get("value");
                    List<Integer> list = new ArrayList<>();
                    for(int i : arrayNBT.getAsIntArray())
                        list.add(i);
                    recipeInfos.addParameter(new RecipeParameterIntList(keys, list));
                    break;
                case MAP:
                    CompoundTag nbt = parameterCompound.getCompound("value");
                    Map<Integer, ResourceLocation> map = new HashMap<>();
                    for(String key : nbt.getAllKeys())
                        map.put(nbt.getInt(key), CommonUtils.parse(key));
                    recipeInfos.addParameter(new RecipeParameterMap(keys, map));
                    break;
            }
        }

        return recipeInfos;
    }

    public static class RecipeParameter
    {
        public static final RecipeParameter EMPTY = new RecipeParameter(RecipeParameterType.EMPTY, "empty");
        private final RecipeParameterType type;
        private final String name;

        public RecipeParameter(RecipeParameterType type, String name)
        {
            this.type = type;
            this.name = name;
        }

        public RecipeParameterType getType()
        {
            return type;
        }

        public String getName()
        {
            return name;
        }
    }

    public static class RecipeParameterNumber extends RecipeParameter
    {
        private final Number value;
        private final boolean isDouble;

        public RecipeParameterNumber(String name, Number value, boolean isDouble)
        {
            super(RecipeParameterType.NUMBER, name);
            this.value = value;
            this.isDouble = isDouble;
        }

        public Number getNumberValue()
        {
            return value;
        }

        public boolean isDouble()
        {
            return isDouble;
        }
    }

    public static class RecipeParameterBoolean extends RecipeParameter
    {
        private final boolean value;

        public RecipeParameterBoolean(String name, boolean value)
        {
            super(RecipeParameterType.BOOLEAN, name);
            this.value = value;
        }

        public boolean getBoolean()
        {
            return value;
        }
    }

    public static class RecipeParameterMap extends RecipeParameter
    {
        private final Map<Integer, ResourceLocation> map;

        public RecipeParameterMap(String name, Map<Integer, ResourceLocation> map)
        {
            super(RecipeParameterType.MAP, name);
            this.map = map;
        }

        public Map<Integer, ResourceLocation> getMap()
        {
            return this.map;
        }
    }

    public static class RecipeParameterIntList extends RecipeParameter
    {
        private final List<Integer> list;

        public RecipeParameterIntList(String name, List<Integer> list)
        {
            super(RecipeParameterType.INT_LIST, name);
            this.list = list;
        }

        public List<Integer> getList()
        {
            return list;
        }
    }

    public static class Parameters
    {
        // Base Parameters
        public static final String SHAPED = "shaped";
        public static final String TAGGED_SLOTS = "tagged_slots";
        public static final String KUBEJS_RECIPE = "kubejs_recipe";
        public static final String NBT_SLOTS = "nbt_slots";

        // Vanilla Parameters
        public static final String EXPERIENCE = "experience";
        public static final String COOKING_TIME = "cooking_time";

        // Botania Parameters
        public static final String TIME = "time";
        public static final String MANA = "mana";

        // Thermal Parameters
        public static final String ENERGY = "energy";
        public static final String ENERGY_MOD = "energy_mod";
        public static final String WATER_MOD = "water_mod";
        public static final String RESIN_AMOUNT = "resin_amount";
        public static final String FLUID_AMOUNT_0 = "fluid_amount_0";
        public static final String FLUID_AMOUNT_1 = "fluid_amount_1";
        public static final String FLUID_AMOUNT_2 = "fluid_amount_2";
        public static final String CHANCE = "chance";
    }

    public enum RecipeParameterType
    {
        NUMBER,
        STRING,
        BOOLEAN,
        INT_LIST,
        EMPTY,
        MAP
    }
}
