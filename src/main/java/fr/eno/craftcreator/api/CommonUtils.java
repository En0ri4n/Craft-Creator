package fr.eno.craftcreator.api;


import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.File;
import java.util.Objects;
import java.util.function.Predicate;

public class CommonUtils
{
    public static final Predicate<RenderType> DEFAULT_BLOCK_RENDER = (r) -> r == RenderType.cutoutMipped();

    public static void setBlockRender(Block block, Predicate<RenderType> render)
    {
        ItemBlockRenderTypes.setRenderLayer(block, render);
    }

    public static void setDefaultBlockRender(Block block)
    {
        setBlockRender(block, DEFAULT_BLOCK_RENDER);
    }

    public static <T extends Recipe<C>, C extends Container> RecipeType<T> getRecipeTypeByName(String resourceLocation)
    {
        return getRecipeTypeByName(ClientUtils.parse(resourceLocation));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<C>, C extends Container> RecipeType<T> getRecipeTypeByName(ResourceLocation resourceLocation)
    {
        return (RecipeType<T>) Registry.RECIPE_TYPE.getOptional(resourceLocation).orElse(null);
    }

    public static ResourceLocation getRecipeTypeName(RecipeType<?> recipeType)
    {
        return Registry.RECIPE_TYPE.getKey(recipeType);
    }

    public static Item getItemById(ResourceLocation resourceLocation)
    {
        return ForgeRegistries.ITEMS.getValue(resourceLocation);
    }

    /**
     * Check if the two entries are equals
     *
     * @param entry1 the first entry
     * @param entry2 the second entry
     * @return true if the two entries are equals
     * @see Objects#equals(Object, Object)
     */
    public static boolean equals(IForgeRegistryEntry<?> entry1, IForgeRegistryEntry<?> entry2)
    {
        return Objects.equals(entry1.getRegistryName(), entry2.getRegistryName());
    }

    /**
     * Create a clickable component which open the specified file
     *
     * @param component the component to display
     * @param toOpen    the file to open
     * @return the clickable component
     */
    public static MutableComponent createComponentFileOpener(MutableComponent component, File toOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, toOpen.getAbsolutePath())));
    }

    /**
     * Create a clickable component which open the specified url
     *
     * @param component the component to display
     * @param urlToOpen the url to open
     * @return the clickable component
     */
    public static MutableComponent createComponentUrlOpener(MutableComponent component, String urlToOpen)
    {
        return component.copy().withStyle((msg) -> msg.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlToOpen)));
    }
}
