package fr.eno.craftcreatorapi.serializer.recipe.utils;

import fr.eno.craftcreatorapi.utils.HasRegistryName;
import fr.eno.craftcreatorapi.utils.Identifier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecipeEntry
{
    private final boolean isTag;
    private final Identifier registryName;
    private final int count;

    public boolean isLucked()
    {
        return false;
    }

    public boolean isBlock()
    {
        return false;
    }

    public static abstract class InputBase<T extends HasRegistryName> extends RecipeEntry
    {
        public InputBase(boolean isTag, Identifier registryName, int count)
        {
            super(isTag, registryName, count);
        }

        public abstract T getItem();
    }

    public static abstract class FluidInputBase<F extends HasRegistryName> extends InputBase<F>
    {
        public FluidInputBase(F fluid, int amount)
        {
            super(false, fluid.getRegistryName(), amount);
        }

        public abstract F getFluid();

        public int getAmount()
        {
            return getCount();
        }
    }

    public static abstract class BlockInputBase<B extends HasRegistryName> extends InputBase<B>
    {
        public BlockInputBase(Identifier registryName)
        {
            super(false, registryName, 1);
        }

        @Override
        public boolean isBlock()
        {
            return true;
        }

        public abstract B getBlock();
    }

    public static abstract class Output<T extends HasRegistryName> extends RecipeEntry
    {
        public Output(Identifier registryName, int count)
        {
            super(false, registryName, count);
        }

        public abstract T getItem();
    }

    public static abstract class FluidOutputBase<F extends HasRegistryName> extends Output<F>
    {
        public FluidOutputBase(F fluid, int amount)
        {
            super(fluid.getRegistryName(), amount);
        }

        public abstract F getFluid();

        public int getAmount()
        {
            return getCount();
        }
    }

    public static abstract class BlockOutput<B extends HasRegistryName> extends Output<B>
    {
        public BlockOutput(Identifier registryName)
        {
            super(registryName, 1);
        }

        public abstract B getBlock();
    }

    public static abstract class LuckedOutputBase<T extends HasRegistryName> extends Output<T>
    {
        private final double chance;

        public LuckedOutputBase(Identifier registryName, int count, double chance)
        {
            super(registryName, count);
            this.chance = chance;
        }

        @Override
        public boolean isLucked()
        {
            return true;
        }

        public double getChance()
        {
            return chance;
        }
    }

    @Getter
    public static class MultiEntry<T extends RecipeEntry>
    {
        private final List<T> entries;

        public MultiEntry()
        {
            this.entries = new ArrayList<>();
        }

        public MultiEntry<T> add(T entry)
        {
            this.entries.add(entry);
            return this;
        }

        public T get(int index)
        {
            return entries.get(index);
        }

        public int size()
        {
            return entries.size();
        }

        public boolean isEmpty()
        {
            return size() <= 0;
        }

    }

    public static class MultiInput<T extends HasRegistryName> extends MultiEntry<InputBase<T>>
    {
        public List<InputBase<T>> getInputs()
        {
            return this.getEntries();
        }
    }

    public static class MultiOutput<T extends HasRegistryName> extends MultiEntry<Output<T>>
    {
        public List<Output<T>> getOutputs()
        {
            return getEntries();
        }

        public Output<T> getOneOutput()
        {
            return this.getOutputs().stream().findFirst().orElse(null);
        }
    }
}