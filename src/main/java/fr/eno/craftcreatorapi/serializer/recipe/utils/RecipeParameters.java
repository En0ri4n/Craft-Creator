package fr.eno.craftcreatorapi.serializer.recipe.utils;

import com.google.gson.*;
import fr.eno.craftcreatorapi.utils.Identifier;
import fr.eno.craftcreatorapi.utils.JsonSerializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RecipeParameters implements JsonSerializable
{
    private static final Gson gson = new GsonBuilder().setStrictness(Strictness.LENIENT).create();

    private final List<RecipeParameter<?>> parameters;

    private RecipeParameters()
    {
        this.parameters = new ArrayList<>();
    }

    public static RecipeParameters create()
    {
        return new RecipeParameters();
    }

    public void addParameter(RecipeParameter<?> parameter)
    {
        this.parameters.add(parameter);
    }

    public RecipeParameter<?> getRecipeParameter(String name)
    {
        return this.parameters.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public Number getValue(String name)
    {
        RecipeParameter<?> parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterNumber ? ((RecipeParameterNumber) parameter).getValue() : -1;
    }

    public boolean getBoolean(String name)
    {
        RecipeParameter<?> parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterBoolean && ((RecipeParameterBoolean) parameter).getValue();
    }

    public Map<Integer, Identifier> getMap(String name)
    {
        RecipeParameter<?> parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterMap ? ((RecipeParameterMap) parameter).getValue() : new HashMap<>();
    }

    public List<Integer> getList(String name)
    {
        RecipeParameter<?> parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterIntList ? ((RecipeParameterIntList) parameter).getValue() : new ArrayList<>();
    }

    public boolean contains(String name)
    {
        return this.getRecipeParameter(name) != null;
    }

    @Override
    public JsonObject serialize()
    {
        JsonObject jsonObject = new JsonObject();

        for(RecipeParameter<?> parameter : this.parameters)
        {
            JsonObject parameterObj = new JsonObject();
            parameterObj.addProperty("type", parameter.getType().name());

            switch(parameter.getType())
            {
                case NUMBER:
                    RecipeParameterNumber number = (RecipeParameterNumber) parameter;
                    parameterObj.addProperty("is_double", number.isDouble());
                    parameterObj.addProperty("value", number.isDouble() ? number.getValue().doubleValue() : number.getValue().intValue());
                    break;
                case BOOLEAN:
                    RecipeParameterBoolean bool = (RecipeParameterBoolean) parameter;
                    parameterObj.addProperty("value", bool.getValue());
                    break;
                case INT_LIST:
                    RecipeParameterIntList intList = (RecipeParameterIntList) parameter;
                    JsonArray jsonArray = new JsonArray();
                    intList.getValue().forEach(jsonArray::add);
                    parameterObj.add("value", jsonArray);
                    break;
                case MAP:
                    RecipeParameterMap map = (RecipeParameterMap) parameter;
                    JsonObject obj = new JsonObject();
                    for(Map.Entry<Integer, Identifier> entry : map.getValue().entrySet())
                        obj.addProperty(entry.getKey().toString(), entry.getValue().toString());
                    parameterObj.add("value", obj);
                    break;
            }

            jsonObject.add(parameter.getName(), parameterObj);
        }
        return jsonObject;
    }

    public static RecipeParameters deserialize(String json)
    {
        RecipeParameters recipeParameters = RecipeParameters.create();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        for(String keys : jsonObject.entrySet().stream().map(Map.Entry::getKey).toList())
        {
            JsonObject parameterCompound = jsonObject.get(keys).getAsJsonObject();
            RecipeParameterType type = RecipeParameterType.valueOf(parameterCompound.get("type").getAsString());

            switch(type)
            {
                case NUMBER:
                    boolean isDouble = parameterCompound.get("is_double").getAsBoolean();
                    Number value = isDouble ? parameterCompound.get("value").getAsDouble() : parameterCompound.get("value").getAsInt();
                    recipeParameters.addParameter(new RecipeParameterNumber(keys, value, isDouble));
                    break;
                case BOOLEAN:
                    recipeParameters.addParameter(new RecipeParameterBoolean(keys, parameterCompound.get("value").getAsBoolean()));
                    break;
                case INT_LIST:
                    JsonArray arrayNBT = parameterCompound.get("value").getAsJsonArray();
                    List<Integer> list = new ArrayList<>();
                    for(JsonElement i : arrayNBT)
                        list.add(i.getAsInt());
                    recipeParameters.addParameter(new RecipeParameterIntList(keys, list));
                    break;
                case MAP:
                    JsonObject obj = parameterCompound.get("value").getAsJsonObject();
                    Map<Integer, Identifier> map = new HashMap<>();
                    for(String key : obj.entrySet().stream().map(Map.Entry::getKey).toList())
                        map.put(Integer.valueOf(key), Identifier.parse(obj.get(key).getAsString()));
                    recipeParameters.addParameter(new RecipeParameterMap(keys, map));
                    break;
            }
        }

        return recipeParameters;
    }

    @Getter
    @AllArgsConstructor
    public static class RecipeParameter<T>
    {
        private final RecipeParameterType type;
        private final String name;
        private final T value;
    }

    @Getter
    public static class RecipeParameterNumber extends RecipeParameter<Number>
    {
        private final boolean isDouble;

        public RecipeParameterNumber(String name, Number value, boolean isDouble)
        {
            super(RecipeParameterType.NUMBER, name, value);
            this.isDouble = isDouble;
        }
    }

    @Getter
    public static class RecipeParameterBoolean extends RecipeParameter<Boolean>
    {
        public RecipeParameterBoolean(String name, boolean value)
        {
            super(RecipeParameterType.BOOLEAN, name, value);
        }
    }

    @Getter
    public static class RecipeParameterMap extends RecipeParameter<Map<Integer, Identifier>>
    {
        public RecipeParameterMap(String name, Map<Integer, Identifier> map)
        {
            super(RecipeParameterType.MAP, name, map);
        }
    }

    @Getter
    public static class RecipeParameterIntList extends RecipeParameter<List<Integer>>
    {
        public RecipeParameterIntList(String name, List<Integer> list)
        {
            super(RecipeParameterType.INT_LIST, name, list);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Parameter<T>
    {
        // Base Parameters
        public static final Parameter<Boolean> SHAPED = new Parameter<>("shaped");
        public static final Parameter<List<Integer>> TAGGED_SLOTS = new Parameter<>("tagged_slots");
        public static final Parameter<SerializerType> KUBEJS_RECIPE = new Parameter<>("kubejs_recipe");
        public static final Parameter<List<Integer>> NBT_SLOTS = new Parameter<>("nbt_slots");

        // Vanilla Parameters
        public static final Parameter<Integer> EXPERIENCE = new Parameter<>("experience");
        public static final Parameter<Integer> COOKING_TIME = new Parameter<>("cooking_time");

        // Botania Parameters
        public static final Parameter<Integer> TIME = new Parameter<>("time");
        public static final Parameter<Integer> MANA = new Parameter<>("mana");

        // Thermal Parameters
        public static final Parameter<Integer> ENERGY = new Parameter<>("energy");
        public static final Parameter<Boolean> ENERGY_MOD = new Parameter<>("energy_mod");
        public static final Parameter<Double> WATER_MOD = new Parameter<>("water_mod");
        public static final Parameter<Integer> RESIN_AMOUNT = new Parameter<>("resin_amount");
        public static final Parameter<Integer> FLUID_AMOUNT_0 = new Parameter<>("fluid_amount_0");
        public static final Parameter<Integer> FLUID_AMOUNT_1 = new Parameter<>("fluid_amount_1");
        public static final Parameter<Integer> FLUID_AMOUNT_2 = new Parameter<>("fluid_amount_2");
        public static final Parameter<Double> CHANCE = new Parameter<>("chance");

        private final String name;
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
