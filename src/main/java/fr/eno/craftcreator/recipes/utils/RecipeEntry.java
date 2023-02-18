package fr.eno.craftcreator.recipes.utils;


import fr.eno.craftcreator.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RecipeEntry
{
    private final boolean isTag;
    private final ResourceLocation registryName;
    private final int count;

    private RecipeEntry(boolean isTag, ResourceLocation registryName, int count)
    {
        this.isTag = isTag;
        this.registryName = registryName;
        this.count = count;
    }

    public boolean isLucked()
    {
        return false;
    }

    public boolean isTag()
    {
        return isTag;
    }

    public boolean isBlock()
    {
        return false;
    }

    public ResourceLocation getRegistryName()
    {
        return registryName;
    }

    public int count()
    {
        return count;
    }

    public static class Input extends RecipeEntry
    {
        public static final Input EMPTY = new Input(false, References.getLoc("empty_input_bug"), 1);

        public Input(boolean isTag, ResourceLocation registryName, int count)
        {
            super(isTag, registryName, count);
        }

        public Item getItem()
        {
            return isTag() ? Items.AIR : ForgeRegistries.ITEMS.getValue(getRegistryName());
        }
    }

    public static class FluidInput extends Input
    {
        public FluidInput(Fluid fluid, int amount)
        {
            super(false, fluid.getRegistryName(), amount);
        }

        public Fluid getFluid()
        {
            return ForgeRegistries.FLUIDS.getValue(getRegistryName());
        }

        public int getAmount()
        {
            return count();
        }
    }

    public static class BlockInput extends Input
    {
        public BlockInput(ResourceLocation registryName)
        {
            super(false, registryName, 1);
        }

        @Override
        public boolean isBlock()
        {
            return true;
        }

        public Block getBlock()
        {
            return ForgeRegistries.BLOCKS.getValue(getRegistryName());
        }
    }

    public static class Output extends RecipeEntry
    {
        public static final Output EMPTY = new Output(References.getLoc("empty_output_bug"), 0);

        public Output(ResourceLocation registryName, int count)
        {
            super(false, registryName, count);
        }

        public Item getItem()
        {
            return ForgeRegistries.ITEMS.getValue(getRegistryName());
        }
    }

    public static class FluidOutput extends Output
    {
        public FluidOutput(Fluid fluid, int amount)
        {
            super(fluid.getRegistryName(), amount);
        }

        public Fluid getFluid()
        {
            return ForgeRegistries.FLUIDS.getValue(getRegistryName());
        }

        public int getAmount()
        {
            return count();
        }
    }

    public static class BlockOutput extends Output
    {
        public BlockOutput(ResourceLocation registryName)
        {
            super(registryName, 1);
        }

        public Block getBlock()
        {
            return ForgeRegistries.BLOCKS.getValue(getRegistryName());
        }
    }

    public static class LuckedOutput extends Output
    {
        private final double chance;

        public LuckedOutput(ResourceLocation registryName, int count, double chance)
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

        public List<T> getEntries()
        {
            return entries;
        }
    }

    public static class MultiInput extends MultiEntry<Input>
    {
        public List<Input> getInputs()
        {
            return this.getEntries();
        }
    }

    public static class MultiOutput extends MultiEntry<Output>
    {
        public List<Output> getOutputs()
        {
            return getEntries();
        }

        public Output getOneOutput()
        {
            return this.getOutputs().stream().findFirst().orElse(Output.EMPTY);
        }
    }
}