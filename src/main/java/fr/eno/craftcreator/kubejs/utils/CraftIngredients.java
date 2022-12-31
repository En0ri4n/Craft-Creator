package fr.eno.craftcreator.kubejs.utils;

import fr.eno.craftcreator.References;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class CraftIngredients
{
    private final List<CraftIngredient> ingredients;

    private CraftIngredients()
    {
        this(new ArrayList<>());
    }

    public CraftIngredients(List<CraftIngredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void addIngredient(CraftIngredient ingredient)
    {
        this.ingredients.add(ingredient);
    }

    public List<CraftIngredient> getIngredients()
    {
        List<CraftIngredient> compactedIngredients = new ArrayList<>();
        List<CraftIngredient> temp = new ArrayList<>(this.ingredients);
        this.ingredients.forEach(ingredient ->
        {
            if(!compactedIngredients.contains(ingredient))
            {
                compactedIngredients.add(ingredient);
                temp.remove(ingredient);
            }
        });

        for(CraftIngredient craftIngredient : compactedIngredients)
        {
            if(craftIngredient instanceof CraftIngredients.ItemIngredient itemIngredient)
            {
                temp.stream().filter(ingredient -> ingredient.equals(itemIngredient)).forEach(ingredient ->
                {
                    if(ingredient instanceof CraftIngredients.ItemIngredient itemIngredient1)
                    {
                        itemIngredient.addCount(itemIngredient1.getCount());
                    }
                });
            }
        }

        return compactedIngredients;
    }

    public boolean isEmpty()
    {
        return this.ingredients.isEmpty();
    }

    public ItemStack getIcon()
    {
        for(CraftIngredient ingredient : this.ingredients)
        {
            if(ingredient instanceof CraftIngredients.ItemIngredient itemIngredient)
            {
                return new ItemStack(ForgeRegistries.ITEMS.getValue(itemIngredient.getId()));
            }
            else if(ingredient instanceof CraftIngredients.FluidIngredient fluidIngredient)
            {
                return new ItemStack(ForgeRegistries.FLUIDS.getValue(fluidIngredient.getId()).getBucket());
            }
        }

        return ItemStack.EMPTY;
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
        public static final CraftIngredient EMPTY = new CraftIngredient(CraftIngredientType.ITEM, new ResourceLocation(References.MOD_ID, "empty"), "empty");
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

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof CraftIngredient ingredient)
            {
                return ingredient.getId().equals(this.getId()) && ingredient.getType().equals(this.getType()) && ingredient.getDescription().equals(this.getDescription());
            }

            return false;
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

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof FluidIngredient ingredient)
            {
                return super.equals(obj) && ingredient.getAmount() == this.getAmount();
            }

            return false;
        }
    }

    public static class ItemIngredient extends CraftIngredient
    {
        private int count;
        private final boolean isTag;

        public ItemIngredient(ResourceLocation id, int count, String description, boolean isTag)
        {
            super(isTag ? CraftIngredientType.TAG : CraftIngredientType.ITEM, id, description);
            this.isTag = isTag;
            this.count = count;
        }

        public ItemIngredient(ResourceLocation id, int count, String description)
        {
            this(id, count, description, false);
        }

        public ItemIngredient(ResourceLocation id, int count)
        {
            this(id, count, "Item");
        }

        public ItemIngredient setCount(int count)
        {
            this.count = count;
            return this;
        }

        public ItemIngredient addCount(int count)
        {
            this.count += count;
            return this;
        }

        public boolean isTag()
        {
            return isTag;
        }

        public int getCount()
        {
            return count;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof ItemIngredient ingredient)
            {
                return super.equals(obj) && ingredient.getCount() == this.getCount();
            }

            return false;
        }
    }

    public static class ItemLuckIngredient extends ItemIngredient
    {
        private double luck;

        public ItemLuckIngredient(ResourceLocation id, int count, double luck, String description, boolean isTag)
        {
            super(id, count, description, isTag);
            this.luck = Double.parseDouble(String.format("%.3f", luck));
        }

        public ItemLuckIngredient(ResourceLocation id, int count, double luck, String description)
        {
            this(id, count, luck, description, false);
        }

        public ItemLuckIngredient(ResourceLocation id, int count, double luck)
        {
            this(id, count, luck, "Item");
        }

        public ItemLuckIngredient setLuck(int luck)
        {
            this.luck = luck;
            return this;
        }

        public double getLuck()
        {
            return luck;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof ItemLuckIngredient ingredient)
            {
                return super.equals(obj) && ingredient.getLuck() == this.getLuck();
            }

            return false;
        }
    }

    public static class BlockIngredient extends CraftIngredient
    {
        private int count;

        public BlockIngredient(ResourceLocation id)
        {
            this(id, 1);
        }

        public BlockIngredient(ResourceLocation id, int count)
        {
            super(CraftIngredientType.BLOCK, id, "Block");
            this.count = count;
        }

        public BlockIngredient setCount(int count)
        {
            this.count = count;
            return this;
        }

        public int getCount()
        {
            return count;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof BlockIngredient ingredient)
            {
                return super.equals(obj) && ingredient.getCount() == this.getCount();
            }

            return false;
        }
    }

    public static class DataIngredient extends CraftIngredient
    {
        private double count;

        public DataIngredient(String description, double count)
        {
            super(CraftIngredientType.DATA, References.getLoc("data"), description);
            this.count = Double.parseDouble(String.format("%.3f", count));
        }

        public DataIngredient setCount(int count)
        {
            this.count = count;
            return this;
        }

        public double getCount()
        {
            return count;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof DataIngredient ingredient)
            {
                return super.equals(obj) && ingredient.getCount() == this.getCount();
            }

            return false;
        }
    }

    public enum CraftIngredientType
    {
        ITEM, TAG, BLOCK, FLUID, DATA
    }
}
