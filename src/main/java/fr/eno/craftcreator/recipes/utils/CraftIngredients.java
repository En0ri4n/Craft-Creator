package fr.eno.craftcreator.recipes.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.eno.craftcreator.References;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemBrewBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class CraftIngredients
{
    public static final CraftIngredient EMPTY = new ItemIngredient(Items.AIR.getRegistryName(), 0);

    private final List<CraftIngredient> ingredients;
    private final List<CraftIngredient> ingredientsWithCount;

    private CraftIngredients()
    {
        this(new ArrayList<>());
    }

    private CraftIngredients(List<CraftIngredient> ingredients)
    {
        this.ingredients = ingredients;
        this.ingredientsWithCount = new ArrayList<>();
    }

    public void addIngredient(CraftIngredient craftIngredient)
    {
        this.ingredients.add(craftIngredient);
    }

    public List<CraftIngredient> getIngredientsWithCount()
    {
        this.ingredients.stream().filter(CraftIngredient::hasCount).forEach(ingredient ->
        {
            ((CountedIngredient) ingredient).addCount((int) (this.ingredients.stream().filter(i -> i.equals(ingredient)).mapToInt(o -> (int) ((CountedIngredient) o).getCount()).sum() - ((CountedIngredient) ingredient).getCount()));
            if(!contains(ingredientsWithCount, ingredient))
                if(ingredient instanceof MultiItemIngredient)
                {
                    if(((MultiItemIngredient) ingredient).getIds().size() > 0)
                        this.ingredientsWithCount.add(ingredient);
                }
                else
                {
                    this.ingredientsWithCount.add(ingredient);
                }


        });

        for(CraftIngredient ingredient : this.ingredients)
        {
            if(!ingredient.hasCount()) this.ingredientsWithCount.add(ingredient);
        }

        return this.ingredientsWithCount;
    }

    private boolean contains(List<CraftIngredient> ingredients, CraftIngredient craftIngredient)
    {
        for(CraftIngredient ingre : ingredients)
        {
            if(ingre.getType().equals(craftIngredient.getType()) && ingre.getId().equals(craftIngredient.getId()) && ingre.getDescription().equals(craftIngredient.getDescription()))
                return true;
        }

        return false;
    }

    public List<CraftIngredient> getIngredients()
    {
        return this.ingredients;
    }

    public boolean containsId(ResourceLocation id)
    {
        return this.ingredients.stream().anyMatch(i -> i.getId().equals(id));
    }

    public boolean containsDescription(String description)
    {
        return this.ingredients.stream().anyMatch(i -> i.getDescription().equals(description));
    }

    public CraftIngredient getByDescription(String description)
    {
        return this.ingredients.stream().filter(i -> i.getDescription().equals(description)).findFirst().orElse(EMPTY);
    }

    public boolean isEmpty()
    {
        return this.ingredients.isEmpty();
    }

    public ItemStack getIcon()
    {
        for(CraftIngredient ingredient : this.ingredients)
        {
            if(ingredient instanceof ItemIngredient)
            {
                if(containsDescription("Brew"))
                {
                    ItemStack stack = ModItems.brewFlask.getDefaultInstance();
                    ItemBrewBase.setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getOrDefault(getByDescription("Brew").getId()));
                    return stack;
                }

                return new ItemStack(ForgeRegistries.ITEMS.getValue(ingredient.getId()));
            }
            else if(ingredient instanceof FluidIngredient)
            {
                return new ItemStack(ForgeRegistries.FLUIDS.getValue(ingredient.getId()).getFilledBucket());
            }
            else if(ingredient instanceof BlockIngredient)
            {
                return new ItemStack(ForgeRegistries.BLOCKS.getValue(ingredient.getId()));
            }
        }

        return new ItemStack(Items.COMMAND_BLOCK);
    }

    public static CraftIngredients create()
    {
        return new CraftIngredients();
    }

    public static CraftIngredients create(List<CraftIngredient> ingredients)
    {
        return new CraftIngredients(ingredients);
    }

    public static class CraftIngredient
    {
        public static final CraftIngredient EMPTY = new CraftIngredient(CraftIngredientType.ITEM, References.getLoc("empty"), "empty");
        private final CraftIngredientType type;
        private final ResourceLocation id;
        private String description;

        private CraftIngredient(CraftIngredientType type, ResourceLocation id, String description)
        {
            this.type = type;
            this.id = id;
            this.description = description;
        }

        public CraftIngredientType getType()
        {
            return type;
        }

        public ResourceLocation getId()
        {
            return id;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public boolean hasCount()
        {
            return false;
        }
    }

    public static class MultiItemIngredient extends CountedIngredient
    {
        private final Map<ResourceLocation, Boolean> ids;

        public MultiItemIngredient(String name, long count)
        {
            super(CraftIngredientType.MULTI_ITEM, References.getLoc(name), "Possibilities", count);
            this.ids = new HashMap<>();
        }

        public void add(ResourceLocation id, boolean isTag)
        {
            this.ids.put(id, isTag);
        }

        public Map<ResourceLocation, Boolean> getIds()
        {
            return ids;
        }
    }

    public static class CountedIngredient extends CraftIngredient
    {
        private long count;

        private CountedIngredient(CraftIngredientType type, ResourceLocation id, String description, long count)
        {
            super(type, id, description);
            this.count = count;
        }

        public void setCount(long count)
        {
            this.count = count;
        }

        public void addCount(int count)
        {
            this.count += count;
        }

        public long getCount()
        {
            return count;
        }

        @Override
        public boolean hasCount()
        {
            return true;
        }
    }

    public static class FluidIngredient extends CraftIngredient
    {
        private int amount;

        public FluidIngredient(ResourceLocation id, int amount, String description)
        {
            super(CraftIngredientType.FLUID, id, description);
            this.amount = amount;
        }

        public FluidIngredient(ResourceLocation id, int amount)
        {
            this(id, amount, "Fluid");
            this.amount = amount;
        }

        public FluidIngredient setAmount(int amount)
        {
            this.amount = amount;
            return this;
        }

        public int getAmount()
        {
            return amount;
        }
    }

    public static class ItemIngredient extends CountedIngredient
    {
        public ItemIngredient(ResourceLocation id, long count, String description)
        {
            super(CraftIngredientType.ITEM, id, description, count);
        }

        public ItemIngredient(ResourceLocation id, long count)
        {
            this(id, count, "Item");
        }
    }

    public static class TagIngredient extends CountedIngredient
    {
        public TagIngredient(ResourceLocation id, long count, String description)
        {
            super(CraftIngredientType.TAG, id, description, count);
        }

        public TagIngredient(ResourceLocation id, long count)
        {
            this(id, count, "Tag");
        }
    }

    public static class ItemLuckIngredient extends ItemIngredient
    {
        private double luck;

        public ItemLuckIngredient(ResourceLocation id, long count, double luck, String description)
        {
            super(id, count, description);
            this.luck = Double.parseDouble(String.format("%.5f", luck));
        }

        public ItemLuckIngredient(ResourceLocation id, long count, double luck)
        {
            this(id, count, luck, "Item");
        }

        public ItemLuckIngredient setLuck(double luck)
        {
            this.luck = luck;
            return this;
        }

        public double getLuck()
        {
            return luck;
        }
    }

    public static class BlockIngredient extends CountedIngredient
    {
        private long count;

        public BlockIngredient(ResourceLocation id)
        {
            this(id, 1);
        }

        public BlockIngredient(ResourceLocation id, long count)
        {
            this(id, count, "Block");
        }

        public BlockIngredient(ResourceLocation id, long count, String description)
        {
            super(CraftIngredientType.BLOCK, id, description, count);
        }
    }

    public static class DataIngredient extends CraftIngredient
    {
        private final DataUnit unit;
        private final boolean isDouble;
        private Number data;

        public DataIngredient(String description, DataUnit unit, Number data, boolean isDouble)
        {
            super(CraftIngredientType.DATA, References.getLoc("data"), description);
            this.unit = unit;
            this.data = isDouble ? Double.parseDouble(String.format("%.5f", data.doubleValue())) : data.intValue();
            this.isDouble = isDouble;
        }

        public boolean isDouble()
        {
            return isDouble;
        }

        public DataIngredient setData(Number data)
        {
            this.data = data;
            return this;
        }

        public Number getData()
        {
            return data;
        }

        public DataUnit getUnit()
        {
            return unit;
        }

        public enum DataUnit
        {
            TICK("tick"), EMPTY(""), EXPERIENCE("xp"), ENERGY("RF");

            private final String unit;

            DataUnit(String unit)
            {
                this.unit = unit;
            }

            public String getDisplayUnit()
            {
                return unit;
            }
        }
    }

    public static class NBTIngredient extends CraftIngredient
    {
        private final Gson gson = new GsonBuilder().setLenient().create();
        private CompoundNBT nbt;

        public NBTIngredient(CompoundNBT nbt)
        {
            this(nbt, "NBT");
        }

        private NBTIngredient(CompoundNBT nbt, String description)
        {
            super(CraftIngredientType.NBT, References.getLoc("nbt"), description);
            this.nbt = nbt;
        }

        public List<String> getNbtList()
        {
            return new ArrayList<>(nbt.keySet());
        }

        public void setNbt(CompoundNBT nbt)
        {
            this.nbt = nbt;
        }

        public CompoundNBT getNbt()
        {
            return nbt;
        }
    }

    public enum CraftIngredientType
    {
        ITEM, TAG, BLOCK, FLUID, MULTI_ITEM, DATA, NBT
    }
}
