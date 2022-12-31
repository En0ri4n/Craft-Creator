package fr.eno.craftcreator.kubejs.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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
        return this.parameters.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public double getDouble(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterDouble ? ((RecipeParameterDouble) parameter).getDoubleValue() : 0D;
    }

    public int getInteger(String name)
    {
        RecipeParameter parameter = this.getRecipeParameter(name);
        return parameter instanceof RecipeParameterInteger ? ((RecipeParameterInteger) parameter).getIntValue() : 0;
    }

    public static class RecipeParameter
    {
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

    public static class RecipeParameterDouble extends RecipeParameter
    {
        private final double value;

        public RecipeParameterDouble(String name, double value)
        {
            super(RecipeParameterType.DOUBLE, name);
            this.value = value;
        }

        public RecipeParameterDouble(String name, String value)
        {
            this(name, value.isEmpty() ? -1D : Double.parseDouble(value));
        }

        public double getDoubleValue()
        {
            return value;
        }
    }

    public static class RecipeParameterInteger extends RecipeParameter
    {
        private final int value;

        public RecipeParameterInteger(String name, int value)
        {
            super(RecipeParameterType.INTEGER, name);
            this.value = value;
        }

        public RecipeParameterInteger(String name, String value)
        {
            this(name, value.isEmpty() ? -1 : Integer.parseInt(value));
        }

        public int getIntValue()
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

    public enum RecipeParameterType
    {
        DOUBLE,
        INTEGER,
        STRING,
        BOOLEAN,
        RESOURCE_LOCATION,
        STRING_LIST
    }
}
