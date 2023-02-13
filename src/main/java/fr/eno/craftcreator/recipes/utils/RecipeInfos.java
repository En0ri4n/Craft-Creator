package fr.eno.craftcreator.recipes.utils;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeInfos
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

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterMap ? ((RecipeParameterMap<K, V>) parameter).getMap() : new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <K> List<K> getList(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterList ? ((RecipeParameterList<K>) parameter).getList() : new ArrayList<>();
    }

    public boolean contains(String name)
    {
        return this.getRecipeParameter(name) != null;
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

        public RecipeParameterNumber(String name, Number value)
        {
            super(RecipeParameterType.NUMBER, name);
            this.value = value;
        }

        public Number getNumberValue()
        {
            return value;
        }
    }

    public static class RecipeParameterString extends RecipeParameter
    {
        private final String value;

        public RecipeParameterString(String name, String value)
        {
            super(RecipeParameterType.STRING, name);
            this.value = value;
        }

        public String getString()
        {
            return value;
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

    public static class RecipeParameterResourceLocation extends RecipeParameter
    {
        private final ResourceLocation value;

        public RecipeParameterResourceLocation(String name, ResourceLocation value)
        {
            super(RecipeParameterType.RESOURCE_LOCATION, name);
            this.value = value;
        }

        public ResourceLocation getResourcelocation()
        {
            return value;
        }
    }

    public static class RecipeParameterStringList extends RecipeParameter
    {
        private final List<String> value;

        public RecipeParameterStringList(String name, List<String> value)
        {
            super(RecipeParameterType.STRING_LIST, name);
            this.value = value;
        }

        public List<String> getStringList()
        {
            return value;
        }
    }

    public static class RecipeParameterMap<K, V> extends RecipeParameter
    {
        private final Map<K, V> map;

        public RecipeParameterMap(String name, Map<K, V> map)
        {
            super(RecipeParameterType.MAP, name);
            this.map = map;
        }

        public RecipeParameterMap(String name)
        {
            this(name, new HashMap<>());
        }

        public Map<K, V> getMap()
        {
            return this.map;
        }
    }

    public static class RecipeParameterList<T> extends RecipeParameter
    {
        private final List<T> list;

        public RecipeParameterList(String name, List<T> list)
        {
            super(RecipeParameterType.INT_LIST, name);
            this.list = list;
        }

        public List<T> getList()
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
        RESOURCE_LOCATION,
        STRING_LIST,
        INT_LIST,
        EMPTY,
        MAP
    }
}
