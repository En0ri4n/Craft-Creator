package fr.eno.craftcreator.recipes.utils;

import com.google.gson.*;
import fr.eno.craftcreator.api.CommonUtils;
import fr.eno.craftcreator.utils.NBTSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public JsonObject serialize()
    {
        JsonObject jsonObject = new JsonObject();
        for(RecipeParameter parameter : this.parameters)
        {
            JsonObject parameterObj = new JsonObject();
            parameterObj.addProperty("type", parameter.getType().name());

            switch(parameter.getType())
            {
                case NUMBER:
                    RecipeParameterNumber number = (RecipeParameterNumber) parameter;
                    parameterObj.addProperty("is_double", number.isDouble());
                    parameterObj.addProperty("value", number.isDouble() ? number.getNumberValue().doubleValue() : number.getNumberValue().intValue());
                    break;
                case BOOLEAN:
                    RecipeParameterBoolean bool = (RecipeParameterBoolean) parameter;
                    parameterObj.addProperty("value", bool.getBoolean());
                    break;
                case INT_LIST:
                    RecipeParameterIntList intList = (RecipeParameterIntList) parameter;
                    JsonArray jsonArray = new JsonArray();
                    intList.getList().forEach(jsonArray::add);
                    parameterObj.add("value", jsonArray);
                    break;
                case MAP:
                    RecipeParameterMap map = (RecipeParameterMap) parameter;
                    JsonObject obj = new JsonObject();
                    for(Map.Entry<Integer, ResourceLocation> entry : map.getMap().entrySet())
                        obj.addProperty(entry.getValue().toString(), entry.getKey());
                    parameterObj.add("value", obj);
                    break;
            }

            jsonObject.add(parameter.getName(), parameterObj);
        }
        return jsonObject;
    }

    public static RecipeInfos deserialize(String json)
    {
        RecipeInfos recipeInfos = RecipeInfos.create();

        final Gson gson = new GsonBuilder().setLenient().create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        for(String keys : jsonObject.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()))
        {
            JsonObject parameterCompound = jsonObject.get(keys).getAsJsonObject();
            RecipeParameterType type = RecipeParameterType.valueOf(parameterCompound.get("type").getAsString());

            switch(type)
            {
                case NUMBER:
                    boolean isDouble = parameterCompound.get("is_double").getAsBoolean();
                    Number value = isDouble ? parameterCompound.get("value").getAsDouble() : parameterCompound.get("value").getAsInt();
                    recipeInfos.addParameter(new RecipeParameterNumber(keys, value, isDouble));
                    break;
                case BOOLEAN:
                    recipeInfos.addParameter(new RecipeParameterBoolean(keys, parameterCompound.get("value").getAsBoolean()));
                    break;
                case INT_LIST:
                    JsonArray arrayNBT = parameterCompound.get("value").getAsJsonArray();
                    List<Integer> list = new ArrayList<>();
                    for(JsonElement i : arrayNBT)
                        list.add(i.getAsInt());
                    recipeInfos.addParameter(new RecipeParameterIntList(keys, list));
                    break;
                case MAP:
                    JsonObject obj = parameterCompound.get("value").getAsJsonObject();
                    Map<Integer, ResourceLocation> map = new HashMap<>();
                    for(String key : obj.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList()))
                        map.put(obj.get(key).getAsInt(), CommonUtils.parse(key));
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
